/*
 * Copyright (c) 2023 European Commission
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

package eu.europa.ec.eudi.iso18013.transfer.response

import android.content.Context
import android.util.Log
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.credential.CredentialRequest
import com.android.identity.credential.CredentialStore
import com.android.identity.credential.NameSpacedData
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.request.DeviceRequestParser
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
import com.android.identity.util.Timestamp
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.DocItem
import eu.europa.ec.eudi.iso18013.transfer.DocRequest
import eu.europa.ec.eudi.iso18013.transfer.DocumentsResolver
import eu.europa.ec.eudi.iso18013.transfer.ReaderAuth
import eu.europa.ec.eudi.iso18013.transfer.RequestedDocumentData
import eu.europa.ec.eudi.iso18013.transfer.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.internal.TAG
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import java.util.ArrayList

/**
 * DeviceResponseGeneratorImpl class is used for parsing a DeviceRequest and generating the DeviceResponse
 *
 * @param context application context
 * @param documentsResolver document manager instance
 * @param storageEngine storage engine used to store documents
 * @param secureArea secure area used to store documents' keys
 */
class DeviceResponseGeneratorImpl(val context: Context,
                                           private val documentsResolver: DocumentsResolver,
                                           private val storageEngine: StorageEngine,
                                           private val secureArea: AndroidKeystoreSecureArea
): ResponseGenerator<DeviceRequest>() {

    private var readerTrustStore: ReaderTrustStore? = null
    private var sessionTranscript: ByteArray? = null

    /**
     * Set a trust store so that reader authentication can be performed.
     *
     * If it is not provided, reader authentication will not be performed.
     *
     * @param readerTrustStore a trust store for reader authentication, e.g. DefaultReaderTrustStore
     */
    override fun setReaderTrustStore(readerTrustStore: ReaderTrustStore) = apply {
        this.readerTrustStore = readerTrustStore
    }

    /**
     * Set a trust store so that reader authentication can be performed.
     *
     * If it is not provided, reader authentication will not be performed.
     *
     * @param readerTrustStore a trust store for reader authentication, e.g. DefaultReaderTrustStore
     */
    fun readerTrustStore(readerTrustStore: ReaderTrustStore) = apply {
        this.readerTrustStore = readerTrustStore
    }

    private val secureAreaRepository: SecureAreaRepository by lazy {
        SecureAreaRepository().apply {
            addImplementation(secureArea)
        }
    }

    /**
     * Creates a response and returns a ResponseResult
     *
     * @param disclosedDocuments a [List] of [DisclosedDocument]
     * @return a [ResponseResult]
     */
    override fun createResponse(
        disclosedDocuments: DisclosedDocuments
    ): ResponseResult {
        try {
            val deviceResponse = DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK)
            disclosedDocuments.documents.forEach { responseDocument ->
                if (responseDocument.docType == "org.iso.18013.5.1.mDL" && responseDocument.selectedDocItems.filter { docItem ->
                        docItem.elementIdentifier.startsWith("age_over_")
                                && docItem.namespace == "org.iso.18013.5.1"
                    }.size > 2) {
                    return ResponseResult.Failure(Exception("Device Response is not allowed to have more than to age_over_NN elements"))
                }
                val addResult =
                    addDocumentToResponse(deviceResponse, responseDocument, sessionTranscript!!)
                if (addResult is AddDocumentToResponse.UserAuthRequired)
                    return ResponseResult.UserAuthRequired(
                        addResult.keyUnlockData.getCryptoObjectForSigning(SecureArea.ALGORITHM_ES256)
                    )
            }
            sessionTranscript = null
            return ResponseResult.Success(DeviceResponse(deviceResponse.generate()))
        } catch (e: Exception) {
            return ResponseResult.Failure(e)
        }
    }

    @Throws(IllegalStateException::class)
    private fun addDocumentToResponse(
        responseGenerator: DeviceResponseGenerator,
        disclosedDocument: DisclosedDocument,
        transcript: ByteArray
    ): AddDocumentToResponse {
        val dataElements = disclosedDocument.selectedDocItems.map {
            CredentialRequest.DataElement(it.namespace, it.elementIdentifier, false)
        }
        val request = CredentialRequest(dataElements)
        val credentialStore = CredentialStore(storageEngine, secureAreaRepository)
        val credential =
            requireNotNull(credentialStore.lookupCredential(disclosedDocument.documentId))
        val authKey = credential.findAuthenticationKey(Timestamp.now())
            ?: throw IllegalStateException("No auth key available")
        val staticAuthData = StaticAuthDataParser(authKey.issuerProvidedData).parse()
        val mergedIssuerNamespaces = MdocUtil.mergeIssuerNamesSpaces(
            request, credential.nameSpacedData, staticAuthData
        )
        val keyUnlockData = AndroidKeystoreSecureArea.KeyUnlockData(authKey.alias)
        try {
            val generator =
                DocumentGenerator(disclosedDocument.docType, staticAuthData.issuerAuth, transcript)
                    .setIssuerNamespaces(mergedIssuerNamespaces)
            generator.setDeviceNamespacesSignature(
                NameSpacedData.Builder().build(),
                authKey.secureArea,
                authKey.alias,
                keyUnlockData,
                SecureArea.ALGORITHM_ES256
            )
            val data = generator.generate()
            responseGenerator.addDocument(data)
        } catch (lockedException: SecureArea.KeyLockedException) {
            Log.e(TAG, "error", lockedException)
            return AddDocumentToResponse.UserAuthRequired(keyUnlockData)
        }
        return AddDocumentToResponse.Success
    }

    /** Parses a request and returns the requested document data
     * @param request the received request
     * @return [RequestedDocumentData]
    */
    override fun parseRequest(request: DeviceRequest): RequestedDocumentData {
        this.sessionTranscript = request.sessionTranscriptBytes
        return DeviceRequestParser()
            .setSessionTranscript(request.sessionTranscriptBytes)
            .setDeviceRequest(request.deviceRequestBytes).parse()
            .documentRequests
            .map {
                DocRequest(
                    it.docType,
                    requestedElementsFrom(it),
                    setReaderAuthResultToDocRequest(it)
                )
            }.flatMap {
                documentsResolver.resolveDocuments(it)
            }.let {
                RequestedDocumentData(it)
            }
    }

    private fun requestedElementsFrom(
        requestedDocument: DeviceRequestParser.DocumentRequest,
    ): ArrayList<DocItem> {
        val result = arrayListOf<DocItem>()
        requestedDocument.namespaces.forEach { namespace ->
            val elements = requestedDocument.getEntryNames(namespace).map { element ->
                DocItem(namespace, element)
            }
            result.addAll(elements)
        }
        return result
    }

    private fun setReaderAuthResultToDocRequest(documentRequest: DeviceRequestParser.DocumentRequest): ReaderAuth? {
        return readerTrustStore?.let { readerTS ->
            documentRequest.readerAuth?.let {
                val trustPath =
                    readerTS.createCertificationTrustPath(documentRequest.readerCertificateChain)
                val certChain = if (trustPath?.isNotEmpty() == true) {
                    trustPath
                } else {
                    documentRequest.readerCertificateChain
                }
                var readerCommonName = ""
                certChain.first().subjectX500Principal.name.split(",")
                    .forEach { line ->
                        val (key, value) = line.split("=", limit = 2)
                        if (key == "CN") {
                            readerCommonName = value
                        }
                    }
                ReaderAuth(
                    it,
                    documentRequest.readerAuthenticated,
                    documentRequest.readerCertificateChain,
                    readerTS.validateCertificationTrustPath(documentRequest.readerCertificateChain),
                    readerCommonName
                )
            }
        }
    }

    private sealed interface AddDocumentToResponse {
        object Success : AddDocumentToResponse
        data class UserAuthRequired(val keyUnlockData: AndroidKeystoreSecureArea.KeyUnlockData) :
            AddDocumentToResponse
    }
}
