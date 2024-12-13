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

import com.android.identity.mdoc.request.DeviceRequestParser
import eu.europa.ec.eudi.iso18013.transfer.internal.getValidIssuedMsoMdocDocuments
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.performReaderAuthentication
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStoreAware
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuth
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.wallet.document.DocType
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.NameSpace

/**
 * Implementation of [RequestProcessor] for [DeviceRequest] for the ISO 18013-5 standard.
 * @property documentManager the document manager to retrieve the requested documents
 * @property readerTrustStore the reader trust store to perform reader authentication
 */
class DeviceRequestProcessor(
    private val documentManager: DocumentManager,
    override var readerTrustStore: ReaderTrustStore? = null
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
            val requestedDocuments =
                DeviceRequestParser(request.deviceRequestBytes, request.sessionTranscriptBytes)
                    .parse()
                    .docRequests
                    .map { docRequest -> docRequest.toRequestedMdocDocuments() }
                    .let { helper.getRequestedDocuments(it) }
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
        fun getRequestedDocuments(
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
                        document = it,
                        requestedItems = docItems,
                        readerAuth = requestedDocument.readerAuthentication.invoke(),
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
     */
    data class RequestedMdocDocument(
        val docType: DocType,
        val requested: Map<NameSpace, Map<ElementIdentifier, Boolean>>,
        val readerAuthentication: () -> ReaderAuth?
    )

    /**
     * Convert the [DeviceRequestParser.DocRequest] to [RequestedMdocDocument].
     * @return the [RequestedMdocDocument]
     */
    private fun DeviceRequestParser.DocRequest.toRequestedMdocDocuments(): RequestedMdocDocument {
        return RequestedMdocDocument(
            docType = docType,
            requested = namespaces.associate { nameSpace ->
                nameSpace to getEntryNames(nameSpace)
                    .associate { elementIdentifier ->
                        elementIdentifier to getIntentToRetain(
                            nameSpace,
                            elementIdentifier
                        )
                    }
            },
            readerAuthentication = {
                readerTrustStore?.performReaderAuthentication(this)
            },
        )
    }
}