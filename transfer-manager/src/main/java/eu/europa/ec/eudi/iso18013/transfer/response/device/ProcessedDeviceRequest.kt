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

import com.android.identity.crypto.Algorithm
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.util.Constants
import eu.europa.ec.eudi.iso18013.transfer.asMap
import eu.europa.ec.eudi.iso18013.transfer.internal.DocumentResponseGenerator.generateDocumentResponse
import eu.europa.ec.eudi.iso18013.transfer.internal.assertAgeOverRequestLimitForIso18013
import eu.europa.ec.eudi.iso18013.transfer.internal.filterWithRequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.internal.getValidIssuedMsoMdocDocumentById
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.wallet.document.DocumentManager

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
        signatureAlgorithm: Algorithm?
    ): ResponseResult {
        try {
            val deviceResponse = DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK)
            disclosedDocuments
                .let {
                    if (includeOnlyRequested) it.filterWithRequestedDocuments(requestedDocuments)
                    else it
                }
                .forEach { disclosedDocument ->
                    val documentResponse = documentManager
                        .getValidIssuedMsoMdocDocumentById(disclosedDocument.documentId)
                        .assertAgeOverRequestLimitForIso18013(disclosedDocument)
                        .generateDocumentResponse(
                            transcript = sessionTranscript,
                            elements = disclosedDocument.disclosedItems.asMap(),
                            keyUnlockData = disclosedDocument.keyUnlockData,
                            signatureAlgorithm = signatureAlgorithm ?: Algorithm.ES256
                        )
                        .getOrThrow()
                    deviceResponse.addDocument(documentResponse)
                }
            return ResponseResult.Success(DeviceResponse(deviceResponse.generate()))
        } catch (e: Exception) {
            return ResponseResult.Failure(e)
        }
    }

}