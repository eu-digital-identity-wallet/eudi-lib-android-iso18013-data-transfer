/*
 * Copyright (c) 2024-2026 European Commission
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
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuthPolicy
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.zkp.ZkResponsePolicy
import eu.europa.ec.eudi.wallet.document.DocumentId
import eu.europa.ec.eudi.wallet.document.DocumentManager
import kotlinx.coroutines.runBlocking
import kotlinx.io.bytestring.ByteString
import org.multipaz.crypto.Algorithm
import org.multipaz.mdoc.response.DeviceResponseGenerator
import org.multipaz.util.Constants
import kotlin.time.ExperimentalTime

/**
 * Implementation of [RequestProcessor.ProcessedRequest.Success] for [DeviceRequest].
 * @property documentManager the document manager to use for resolving documents
 * @property sessionTranscript the session transcript
 * @property requestedDocuments the requested documents
 * @property readerAuthPolicy the policy for enforcing reader authentication results. Default is [ReaderAuthPolicy.EnforceIfPresent].
 * @property zkResponsePolicy the policy to use when ZK proof generation fails. Default is [ZkResponsePolicy.FallbackToFullDisclosure].
 */
class ProcessedDeviceRequest(
    private val documentManager: DocumentManager,
    private val sessionTranscript: ByteArray,
    requestedDocuments: RequestedDocuments,
    private val readerAuthPolicy: ReaderAuthPolicy = ReaderAuthPolicy.EnforceIfPresent,
    private val zkResponsePolicy: ZkResponsePolicy = ZkResponsePolicy.FallbackToFullDisclosure,
) : RequestProcessor.ProcessedRequest.Success(requestedDocuments) {

    /**
     * Generate the response for the disclosed documents.
     * @param disclosedDocuments the disclosed documents
     * @param signatureAlgorithm the signature algorithm to use for the document responses
     * @return the response result with the device response or the error
     */
    @OptIn(ExperimentalTime::class)
    override fun generateResponse(
        disclosedDocuments: DisclosedDocuments,
        signatureAlgorithm: Algorithm? // TODO: signatureAlgorithm remove this parameter ?
    ): ResponseResult {
        try {
            val documentIds = mutableListOf<DocumentId>()
            val deviceResponseGenerator =
                DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK)
            disclosedDocuments
                .filterWithRequestedDocuments(requestedDocuments)
                .forEachIndexed { index, disclosedDocument ->
                    val requestedDocument = requestedDocuments.find {
                        it.documentId == disclosedDocument.documentId
                    }

                    // Enforce reader authentication based on the configured policy
                    val readerAuth = requestedDocument?.readerAuth
                    val skipByPolicy = when (readerAuthPolicy) {
                        ReaderAuthPolicy.DoNotEnforce -> false
                        ReaderAuthPolicy.EnforceIfPresent -> readerAuth?.isVerified == false
                        ReaderAuthPolicy.AlwaysRequire -> readerAuth?.isVerified != true
                    }
                    if (skipByPolicy) return@forEachIndexed

                    val encodedDocument = runBlocking {
                        documentManager.getValidIssuedMsoMdocDocumentById(disclosedDocument.documentId)
                    }.assertAgeOverRequestLimitForIso18013(disclosedDocument)
                        .generateDocumentResponse(
                            transcript = sessionTranscript,
                            elements = disclosedDocument.disclosedItems.asMap(),
                            keyUnlockData = disclosedDocument.keyUnlockData
                        )
                        .getOrThrow()

                    // Check for matched ZK system for the disclosed document
                    // If found, generate ZK proof, else use encoded document
                    val matchedZkSystem = requestedDocument?.matchedZkSystem

                    if (matchedZkSystem == null) {
                        // No matched ZK system, add encoded document
                        deviceResponseGenerator.addDocument(encodedDocument)
                    } else {
                        when (zkResponsePolicy) {
                            ZkResponsePolicy.Strict -> {
                                val zkDocument = matchedZkSystem.system.generateProof(
                                    zkSystemSpec = matchedZkSystem.spec,
                                    encodedDocument = ByteString(encodedDocument),
                                    encodedSessionTranscript = ByteString(sessionTranscript)
                                )
                                deviceResponseGenerator.addZkDocument(zkDocument)
                            }

                            ZkResponsePolicy.FallbackToFullDisclosure -> {
                                runCatching {
                                    matchedZkSystem.system.generateProof(
                                        zkSystemSpec = matchedZkSystem.spec,
                                        encodedDocument = ByteString(encodedDocument),
                                        encodedSessionTranscript = ByteString(sessionTranscript)
                                    )
                                }.onSuccess { zkDocument ->
                                    deviceResponseGenerator.addZkDocument(zkDocument)
                                }.onFailure {
                                    deviceResponseGenerator.addDocument(encodedDocument)
                                }
                            }
                        }
                    }
                    documentIds.add(disclosedDocument.documentId)
                }
            return ResponseResult.Success(
                DeviceResponse(
                    deviceResponseBytes = deviceResponseGenerator.generate(),
                    sessionTranscriptBytes = sessionTranscript,
                    documentIds = documentIds
                )
            )
        } catch (e: Exception) {
            return ResponseResult.Failure(e)
        }
    }
}