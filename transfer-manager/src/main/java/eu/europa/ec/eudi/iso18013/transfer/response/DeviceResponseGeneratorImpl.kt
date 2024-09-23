/*
 * Copyright (c) 2023-2024 European Commission
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
import com.android.identity.android.securearea.AndroidKeystoreKeyUnlockData
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.credential.CredentialFactory
import com.android.identity.crypto.Algorithm
import com.android.identity.crypto.javaX509Certificates
import com.android.identity.document.Document
import com.android.identity.document.DocumentRequest
import com.android.identity.document.DocumentStore
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.credential.MdocCredential
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.request.DeviceRequestParser
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.KeyLockedException
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * DeviceResponseGeneratorImpl class is used for parsing a DeviceRequest and generating the DeviceResponse
 *
 * @param context application context
 * @param documentsResolver document manager instance
 * @param storageEngine storage engine used to store documents
 * @param secureArea secure area used to store documents' keys
 */
class DeviceResponseGeneratorImpl(
    val context: Context,
    private val documentsResolver: DocumentsResolver,
    private val storageEngine: StorageEngine,
    private val secureArea: AndroidKeystoreSecureArea
) : ResponseGenerator<DeviceRequest> {

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

    private val credentialFactory: CredentialFactory by lazy {
        CredentialFactory().apply {
            addCredentialImplementation(MdocCredential::class) { document, dataItem ->
                MdocCredential(document, dataItem)
            }
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
                        addResult.keyUnlockData.getCryptoObjectForSigning(Algorithm.ES256)
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
            DocumentRequest.DataElement(it.namespace, it.elementIdentifier, false)
        }
        val request = DocumentRequest(dataElements)
        val documentStore =
            DocumentStore(storageEngine, secureAreaRepository, credentialFactory)
        val document = documentStore.lookupDocument(disclosedDocument.documentId)
            ?: throw IllegalStateException("Document not found")
        val credential = document.findCredential(Clock.System.now())
            ?: throw IllegalStateException("No credential available")
        val staticAuthData = StaticAuthDataParser(credential.issuerProvidedData).parse()
        val mergedIssuerNamespaces = MdocUtil.mergeIssuerNamesSpaces(
            request, document.applicationData.getNameSpacedData("nameSpacedData"), staticAuthData
        )
        val keyUnlockData = AndroidKeystoreKeyUnlockData(credential.alias)
        try {
            val generator =
                DocumentGenerator(disclosedDocument.docType, staticAuthData.issuerAuth, transcript)
                    .setIssuerNamespaces(mergedIssuerNamespaces)
            generator.setDeviceNamespacesSignature(
                NameSpacedData.Builder().build(),
                credential.secureArea,
                credential.alias,
                keyUnlockData,
                Algorithm.ES256
            )
            val data = generator.generate()
            responseGenerator.addDocument(data)
        } catch (lockedException: KeyLockedException) {
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
        return DeviceRequestParser(request.deviceRequestBytes, request.sessionTranscriptBytes)
            .parse()
            .docRequests
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
        requestedDocument: DeviceRequestParser.DocRequest,
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

    private fun setReaderAuthResultToDocRequest(documentRequest: DeviceRequestParser.DocRequest): ReaderAuth? {
        val trustStore = readerTrustStore ?: return null
        val readerAuth = documentRequest.readerAuth ?: return null
        val readerCertificateChain = documentRequest.readerCertificateChain ?: return null
        if (documentRequest.readerCertificateChain?.javaX509Certificates?.isEmpty() == true) return null

        val certChain =
            trustStore.createCertificationTrustPath(readerCertificateChain.javaX509Certificates)
                ?.takeIf { it.isNotEmpty() } ?: readerCertificateChain.javaX509Certificates

        val readerCommonName = certChain.firstOrNull()
            ?.subjectX500Principal
            ?.name
            ?.split(",")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it.size == 2 && it[0] == "CN" }
            ?.get(1)
            ?.trim()
            ?: ""
        return ReaderAuth(
            readerAuth,
            documentRequest.readerAuthenticated,
            readerCertificateChain.javaX509Certificates,
            trustStore.validateCertificationTrustPath(readerCertificateChain.javaX509Certificates),
            readerCommonName
        )
    }

    private sealed interface AddDocumentToResponse {
        object Success : AddDocumentToResponse
        data class UserAuthRequired(val keyUnlockData: AndroidKeystoreKeyUnlockData) :
            AddDocumentToResponse
    }

    private fun Document.findCredential(
        now: Instant
    ): MdocCredential? {
        var candidate: MdocCredential? = null
        certifiedCredentials
            .filterIsInstance<MdocCredential>()
            .filter { now >= it.validFrom && now <= it.validUntil }
            .forEach { credential ->
                // If we already have a candidate, prefer this one if its usage count is lower
                candidate?.let { candidateCredential ->
                    if (credential.usageCount < candidateCredential.usageCount) {
                        candidate = credential
                    }
                } ?: run {
                    candidate = credential
                }

            }
        return candidate
    }
}
