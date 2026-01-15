/*
 * Copyright (c) 2024 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.europa.ec.eudi.iso18013.transfer.response.device

import eu.europa.ec.eudi.iso18013.transfer.internal.getValidIssuedMsoMdocDocuments
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.performReaderAuthentication
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStoreAware
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuth
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.zkp.MatchedZkSystem
import eu.europa.ec.eudi.iso18013.transfer.zkp.findMatchedZkSystem
import eu.europa.ec.eudi.wallet.document.DocType
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.NameSpace
import kotlinx.coroutines.runBlocking
import org.multipaz.cbor.Cbor
import org.multipaz.cbor.DataItem
import org.multipaz.mdoc.request.DocRequest
import org.multipaz.mdoc.zkp.ZkSystemRepository
import org.multipaz.mdoc.request.DeviceRequest as MultipazDeviceRequest

/**
 * Implementation of [RequestProcessor] for [DeviceRequest] for the ISO 18013-5 standard.
 * @property documentManager the document manager to retrieve the requested documents
 * @property readerTrustStore the reader trust store to perform reader authentication
 * @property zkSystemRepository the zero-knowledge proof system repository
 */
class DeviceRequestProcessor(
    private val documentManager: DocumentManager,
    override var readerTrustStore: ReaderTrustStore? = null,
    private var zkSystemRepository: ZkSystemRepository? = null
) : RequestProcessor, ReaderTrustStoreAware {

    /**
     * The helper class to process the [RequestedMdocDocument] and return the [RequestedDocuments].
     */
    private val helper: Helper by lazy {
        Helper(documentManager)
    }

    /**
     * Process the [DeviceRequest] and return the [ProcessedDeviceRequest] or a [RequestProcessor.ProcessedRequest.Failure].
     * @param request the [DeviceRequest] to process
     * @return the [ProcessedDeviceRequest] or a [RequestProcessor.ProcessedRequest.Failure]
     */
    override fun process(request: Request): RequestProcessor.ProcessedRequest {
        try {
            require(request is DeviceRequest) { "Request must be a DeviceRequest" }
            val requestedDocuments = runBlocking {
                val deviceRequestDataItem: DataItem = Cbor.decode(request.deviceRequestBytes)
                val sessionTranscriptDataItem: DataItem =
                    Cbor.decode(request.sessionTranscriptBytes)
                val parsedRequest = MultipazDeviceRequest.fromDataItem(deviceRequestDataItem).apply {
                    // It is important to call 'verifyReaderAuthentication' here.
                    // Otherwise, an "IllegalStateException: readerAuth not verified" exception
                    // may be thrown later when try to access 'DocRequest'.
                    // We ignore the result of 'verifyReaderAuthentication' here.
                    // The reader authentication is performed on later stage based on
                    // the given or not readerTrustStore.
                    runCatching {
                        verifyReaderAuthentication(sessionTranscriptDataItem)
                    }
                }

                parsedRequest.docRequests
                    .map { docRequest ->
                        docRequest.toRequestedMdocDocuments(
                            parsedRequest = parsedRequest,
                            sessionTranscript = sessionTranscriptDataItem
                        )
                    }
                    .let { helper.getRequestedDocuments(it) }
            }
            return ProcessedDeviceRequest(
                documentManager = documentManager,
                requestedDocuments = requestedDocuments,
                sessionTranscript = request.sessionTranscriptBytes
            )
        } catch (e: Throwable) {
            return RequestProcessor.ProcessedRequest.Failure(e)
        }
    }

    /**
     * Helper class to process the [RequestedMdocDocument] and return the [RequestedDocuments].
     * @property documentManager the document manager to retrieve the requested documents
     */
    class Helper(
        private val documentManager: DocumentManager,
    ) {
        /**
         * Get the [RequestedDocuments] from the [RequestedMdocDocument].
         * @param requestedMdocDocuments the [RequestedMdocDocument] to process
         * @return the [RequestedDocuments]
         */
        suspend fun getRequestedDocuments(
            requestedMdocDocuments: List<RequestedMdocDocument>
        ): RequestedDocuments {
            return requestedMdocDocuments.flatMap { requestedDocument ->
                val docItems =
                    requestedDocument.requested.flatMap { (nameSpace, elementIdentifiers) ->
                        elementIdentifiers.map { (elementIdentifier, intentToRetain) ->
                            MsoMdocItem(
                                namespace = nameSpace,
                                elementIdentifier = elementIdentifier,
                            ) to intentToRetain
                        }
                    }.toMap()

                documentManager.getValidIssuedMsoMdocDocuments(requestedDocument.docType).map {
                    RequestedDocument(
                        documentId = it.id,
                        requestedItems = docItems,
                        readerAuth = requestedDocument.readerAuthentication.invoke(),
                        matchedZkSystem = requestedDocument.matchedZkSystem
                    )
                }
            }.let { RequestedDocuments(it) }
        }
    }

    /**
     * Parsed requested document.
     * @property docType the document type
     * @property requested the requested elements
     * @property readerAuthentication the reader authentication
     * @property matchedZkSystem the matched zero-knowledge proof system and its specification, if any
     */
    data class RequestedMdocDocument(
        val docType: DocType,
        val requested: Map<NameSpace, Map<ElementIdentifier, Boolean>>,
        val readerAuthentication: () -> ReaderAuth?,
        val matchedZkSystem: MatchedZkSystem? = null
    )

    /**
     * Convert multipaz [org.multipaz.mdoc.request.DocRequest] to [RequestedMdocDocument].
     * @param parsedRequest The full parsed DeviceRequest for signature verification
     * @param sessionTranscript The session transcript DataItem for verification
     * @return the [RequestedMdocDocument]
     */
    private fun DocRequest.toRequestedMdocDocuments(
        parsedRequest: MultipazDeviceRequest,
        sessionTranscript: DataItem
    ): RequestedMdocDocument {
        return RequestedMdocDocument(
            docType = docType,
            requested = nameSpaces.mapValues { (_, dataElements) ->
                dataElements.mapKeys { (elementName, _) -> elementName }
            },
            readerAuthentication = {
                readerTrustStore?.performReaderAuthentication(
                    docRequest = this,
                    parsedRequest = parsedRequest,
                    sessionTranscript = sessionTranscript
                )
            },
            matchedZkSystem = zkSystemRepository?.let { findMatchedZkSystem(it) }
        )
    }
}