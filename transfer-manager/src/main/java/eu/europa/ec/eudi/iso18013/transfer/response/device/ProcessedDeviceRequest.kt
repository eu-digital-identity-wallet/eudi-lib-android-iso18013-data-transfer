/*
 * Copyright (c) 2024-2025 European Commission
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

import eu.europa.ec.eudi.iso18013.transfer.asMap
import eu.europa.ec.eudi.iso18013.transfer.internal.DocumentResponseGenerator.generateDocumentResponse
import eu.europa.ec.eudi.iso18013.transfer.internal.assertAgeOverRequestLimitForIso18013
import eu.europa.ec.eudi.iso18013.transfer.internal.filterWithRequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.internal.getValidIssuedMsoMdocDocumentById
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.wallet.document.DocumentId
import eu.europa.ec.eudi.wallet.document.DocumentManager
import kotlinx.coroutines.runBlocking
import org.multipaz.crypto.Algorithm
import org.multipaz.mdoc.response.DeviceResponseGenerator
import org.multipaz.util.Constants

/**
 * Implementation of [RequestProcessor.ProcessedRequest.Success] for [DeviceRequest].
 * @property documentManager the document manager to use for resolving documents
 * @property sessionTranscript the session transcript
 * @property requestedDocuments the requested documents
 * @property includeOnlyRequested whether to include only the requested documents or all the disclosed documents. Default is true.
 */
class ProcessedDeviceRequest(
    private val documentManager: DocumentManager,
    private val sessionTranscript: ByteArray,
    requestedDocuments: RequestedDocuments
) : RequestProcessor.ProcessedRequest.Success(requestedDocuments) {

    var includeOnlyRequested: Boolean = true

    /**
     * Generate the response for the disclosed documents.
     * @param disclosedDocuments the disclosed documents
     * @param signatureAlgorithm the signature algorithm to use for the document responses
     * @return the response result with the device response or the error
     */
    override fun generateResponse(
        disclosedDocuments: DisclosedDocuments,
        signatureAlgorithm: Algorithm? // TODO: signatureAlgorithm remove this parameter ?
    ): ResponseResult {
        try {
            val documentIds = mutableListOf<DocumentId>()
            val deviceResponse = DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK)
            disclosedDocuments
                .let {
                    if (includeOnlyRequested) it.filterWithRequestedDocuments(requestedDocuments)
                    else it
                }
                .forEachIndexed { index, disclosedDocument ->
                    val documentResponse = runBlocking {
                        documentManager.getValidIssuedMsoMdocDocumentById(disclosedDocument.documentId)
                    }.assertAgeOverRequestLimitForIso18013(disclosedDocument)
                        .generateDocumentResponse(
                            transcript = sessionTranscript,
                            elements = disclosedDocument.disclosedItems.asMap(),
                            keyUnlockData = disclosedDocument.keyUnlockData
                        )
                        .getOrThrow()
                    deviceResponse.addDocument(documentResponse)
                    documentIds.add(disclosedDocument.documentId)
                }
            return ResponseResult.Success(
                DeviceResponse(
                    deviceResponseBytes = deviceResponse.generate(),
                    sessionTranscriptBytes = sessionTranscript,
                    documentIds = documentIds
                )
            )
        } catch (e: Exception) {
            return ResponseResult.Failure(e)
        }
    }

}