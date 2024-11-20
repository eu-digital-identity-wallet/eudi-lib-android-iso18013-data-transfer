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
import eu.europa.ec.eudi.iso18013.transfer.response.DocItem
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.wallet.document.DocumentManager

/**
 * Implementation of [RequestProcessor] for [DeviceRequest] for the ISO 18013-5 standard.
 * @property documentManager the document manager to retrieve the requested documents
 * @property readerTrustStore the reader trust store to perform reader authentication
 */
class DeviceRequestProcessor(
    private val documentManager: DocumentManager,
    val readerTrustStore: ReaderTrustStore? = null
) : RequestProcessor {
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
                    .flatMap { docRequest ->
                        val docItems = docRequest.namespaces.flatMap { nameSpace ->
                            docRequest.getEntryNames(nameSpace).map { elementIdentifier ->
                                DocItem(
                                    namespace = nameSpace,
                                    elementIdentifier = elementIdentifier,
                                )
                            }
                        }
                        documentManager.getValidIssuedMsoMdocDocuments(docRequest.docType).map {
                            RequestedDocument(
                                documentId = it.id,
                                requestedItems = docItems.associateWith {
                                    docRequest.getIntentToRetain(it.namespace, it.elementIdentifier)
                                },
                                readerAuth = readerTrustStore?.performReaderAuthentication(
                                    docRequest
                                ),
                            )
                        }
                    }
                    .let { RequestedDocuments(it) }
            return ProcessedDeviceRequest(
                documentManager = documentManager,
                requestedDocuments = requestedDocuments,
                sessionTranscript = request.sessionTranscriptBytes
            )
        } catch (e: Throwable) {
            return RequestProcessor.ProcessedRequest.Failure(e)
        }
    }
}