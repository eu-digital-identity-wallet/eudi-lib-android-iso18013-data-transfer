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
package eu.europa.ec.eudi.iso18013.transfer.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.identity.android.mdoc.deviceretrieval.DeviceRetrievalHelper
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.KeyUnlockData
import com.android.identity.credential.CredentialRequest
import com.android.identity.credential.CredentialStore
import com.android.identity.credential.NameSpacedData
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.origininfo.OriginInfo
import com.android.identity.mdoc.origininfo.OriginInfoReferrerUrl
import com.android.identity.mdoc.request.DeviceRequestParser
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureArea.ALGORITHM_ES256
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
import com.android.identity.util.Timestamp
import eu.europa.ec.eudi.iso18013.transfer.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.DocItem
import eu.europa.ec.eudi.iso18013.transfer.DocRequest
import eu.europa.ec.eudi.iso18013.transfer.DocumentsResolver
import eu.europa.ec.eudi.iso18013.transfer.ReaderAuth
import eu.europa.ec.eudi.iso18013.transfer.Request
import eu.europa.ec.eudi.iso18013.transfer.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.TransferEvent
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.engagement.QrCode
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import java.util.OptionalLong

private const val TRANSFER_STARTED_MSG = "Transfer has already started."

/**
 * Transfer Manager class used for performing device engagement and data retrieval.
 *
 * @param context application context
 * @param documentsResolver document manager instance
 * @param storageEngine storage engine used to store documents
 * @param secureArea secure area used to store documents' keys
 * @constructor Create empty Transfer manager
 */
internal class TransferManagerImpl(
    context: Context,
    private val documentsResolver: DocumentsResolver,
    private val storageEngine: StorageEngine,
    private val secureArea: AndroidKeystoreSecureArea,
) : TransferManager {
    private val context = context.applicationContext

    private var deviceRetrievalHelper: DeviceRetrievalHelper? = null

    private var engagementToApp: EngagementToApp? = null
    private var qrEngagement: QrEngagement? = null
    private var hasStarted = false

    private val secureAreaRepository: SecureAreaRepository by lazy {
        SecureAreaRepository().apply {
            addImplementation(secureArea)
        }
    }

    private var readerTrustStore: ReaderTrustStore? = null

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

    private val transferEventListeners = mutableListOf<TransferEvent.Listener>()

    /**
     * Add a transfer event listener
     *
     * @param listener a transfer event listener
     */
    override fun addTransferEventListener(listener: TransferEvent.Listener) = apply {
        transferEventListeners.add(listener)
    }

    /**
     * Remove a transfer event listener
     *
     * @param listener a transfer event listener
     */
    override fun removeTransferEventListener(listener: TransferEvent.Listener) = apply {
        transferEventListeners.remove(listener)
    }

    /**
     * Remove all transfer event listeners
     */
    override fun removeAllTransferEventListeners() = apply {
        transferEventListeners.clear()
    }

    private var retrievalMethods = mutableListOf<DeviceRetrievalMethod>()

    override fun setRetrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>) = apply {
        this.retrievalMethods = ArrayList(retrievalMethods)
    }

    /**
     * Starts the QR Engagement and generates the QR code
     *
     * Once the QR code is ready, you will receive the event [TransferEvent.QrEngagementReady]
     *
     */
    override fun startQrEngagement() {
        if (hasStarted) {
            Log.d(this.TAG, TRANSFER_STARTED_MSG)
            transferEventListeners.onTransferEvent(
                TransferEvent.Error(
                    IllegalStateException(
                        TRANSFER_STARTED_MSG,
                    ),
                ),
            )
            return
        }
        qrEngagement = QrEngagement(
            context = context,
            retrievalMethods = retrievalMethods,
            onConnecting = {
                transferEventListeners.onTransferEvent(TransferEvent.Connecting)
            },
            onQrEngagementReady = {
                transferEventListeners.onTransferEvent(
                    TransferEvent.QrEngagementReady(
                        QrCode(
                            qrEngagement?.deviceEngagementUriEncoded ?: "",
                        ),
                    ),
                )
            },
            onDeviceRetrievalHelperReady = { deviceRetrievalHelper ->
                this.deviceRetrievalHelper = deviceRetrievalHelper
                transferEventListeners.onTransferEvent(TransferEvent.Connected)
            },
            onNewDeviceRequest = { deviceRequestBytes ->
                val request = parseRequestBytes(deviceRequestBytes)
                transferEventListeners.onTransferEvent(TransferEvent.RequestReceived(request))
            },
            onDisconnected = {
                transferEventListeners.onTransferEvent(TransferEvent.Disconnected)
            },
            onCommunicationError = { error ->
                Log.d(this.TAG, "onError: ${error.message}")
                transferEventListeners.onTransferEvent(TransferEvent.Error(error))
            },
        ).apply { configure() }

        hasStarted = true
    }

    override fun setupNfcEngagement(service: NfcEngagementService) = apply {
        service.apply {
            retrievalMethods = this@TransferManagerImpl.retrievalMethods
            onConnecting = {
                transferEventListeners.onTransferEvent(TransferEvent.Connecting)
            }
            onDeviceRetrievalHelperReady = { deviceRetrievalHelper ->
                this@TransferManagerImpl.deviceRetrievalHelper = deviceRetrievalHelper
                transferEventListeners.onTransferEvent(TransferEvent.Connected)
            }
            onNewDeviceRequest = { deviceRequestBytes ->
                val request = parseRequestBytes(deviceRequestBytes)
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        request,
                    ),
                )
            }
            onDisconnected = {
                transferEventListeners.onTransferEvent(TransferEvent.Disconnected)
            }
            onCommunicationError = { error ->
                Log.d(this.TAG, "onError: ${error.message}")
                transferEventListeners.onTransferEvent(TransferEvent.Error(error))
            }
        }
    }

    /**
     * Starts the engagement to app, according to ISO 18013-7.
     *
     * @param intent The intent being received
     */
    override fun startEngagementToApp(intent: Intent) {
        if (hasStarted) {
            Log.d(this.TAG, TRANSFER_STARTED_MSG)
            transferEventListeners.onTransferEvent(
                TransferEvent.Error(
                    IllegalStateException(
                        TRANSFER_STARTED_MSG,
                    ),
                ),
            )
            return
        }
        Log.d(this.TAG, "New intent $intent")

        var mdocUri: String? = null
        var mdocReferrerUri: String? = null
        if (intent.scheme.equals("mdoc")) {
            val uri = Uri.parse(intent.toUri(0))
            mdocUri = "mdoc://" + uri.authority
            mdocReferrerUri = intent.extras?.getString(Intent.EXTRA_REFERRER)
        }

        if (mdocUri == null) {
            Log.e(this.TAG, "No mdoc:// URI")
            return
        }
        Log.i(this.TAG, "uri: $mdocUri")

        val originInfos = ArrayList<OriginInfo>()
        if (mdocReferrerUri == null) {
            Log.w(this.TAG, "No referrer URI")
        } else {
            Log.i(this.TAG, "referrer: $mdocReferrerUri")
            originInfos.add(OriginInfoReferrerUrl(mdocReferrerUri))
        }

        engagementToApp = EngagementToApp(
            context = context,
            dataTransportOptions = retrievalMethods.transportOptions,
            onPresentationReady = { deviceRetrievalHelper ->
                this.deviceRetrievalHelper = deviceRetrievalHelper
                transferEventListeners.onTransferEvent(TransferEvent.Connected)
            },
            onNewRequest = { deviceRequestBytes ->
                val request = parseRequestBytes(deviceRequestBytes)
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(request),
                )
            },
            onDisconnected = {
                transferEventListeners.onTransferEvent(TransferEvent.Disconnected)
            },
            onCommunicationError = { error ->
                Log.d(this.TAG, "onError: ${error.message}")
                transferEventListeners.onTransferEvent(TransferEvent.Error(error))
            },
        ).apply {
            configure(mdocUri, originInfos)
        }
        hasStarted = true
    }

    private fun parseRequestBytes(deviceRequestBytes: ByteArray): Request {
        return DeviceRequestParser()
            .setSessionTranscript(deviceRetrievalHelper?.sessionTranscript ?: byteArrayOf(0))
            .setDeviceRequest(deviceRequestBytes).parse()
            .documentRequests
            .map {
                DocRequest(
                    it.docType,
                    requestedElementsFrom(it),
                    it.itemsRequest,
                    setReaderAuthResultToDocRequest(it),
                )
            }.flatMap {
                documentsResolver.resolveDocuments(it)
            }.let {
                Request(it)
            }
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
                    readerCommonName,
                )
            }
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

    /**
     * Create document response
     *
     * @param disclosedDocuments a [List] of [Request]
     * @return a [ResponseResult]
     */
    @Throws(IllegalStateException::class)
    override fun createResponse(disclosedDocuments: DisclosedDocuments): ResponseResult {
        try {
            val deviceResponse = DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK)
            disclosedDocuments.documents.forEach { responseDocument ->
                val addResult = addDocumentToResponse(deviceResponse, responseDocument)
                if (addResult is AddDocumentToResponse.UserAuthRequired) {
                    return ResponseResult.UserAuthRequired(
                        addResult.keyUnlockData.getCryptoObjectForSigning(ALGORITHM_ES256),
                    )
                }
            }
            return ResponseResult.Response(deviceResponse.generate())
        } catch (e: Exception) {
            return ResponseResult.Failure(e)
        }
    }

    override fun sendResponse(responseBytes: ByteArray) {
        val progressListener: (Long, Long) -> Unit = { progress, max ->
            Log.d(this.TAG, "Progress: $progress of $max")
            if (progress == max) Log.d(this.TAG, "Completed...")
        }
        deviceRetrievalHelper?.sendDeviceResponse(
            responseBytes,
            OptionalLong.of(Constants.SESSION_DATA_STATUS_SESSION_TERMINATION),
            progressListener,
            context.mainExecutor(),
        )
        transferEventListeners.onTransferEvent(TransferEvent.ResponseSent)
    }

    @Throws(IllegalStateException::class)
    private fun addDocumentToResponse(
        responseGenerator: DeviceResponseGenerator,
        disclosedDocument: DisclosedDocument,
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
            request,
            credential.nameSpacedData,
            staticAuthData,
        )
        val transcript = deviceRetrievalHelper?.sessionTranscript ?: byteArrayOf(0)
        val keyUnlockData = KeyUnlockData(authKey.alias)
        try {
            val generator =
                DocumentGenerator(disclosedDocument.docType, staticAuthData.issuerAuth, transcript)
                    .setIssuerNamespaces(mergedIssuerNamespaces)
            generator.setDeviceNamespacesSignature(
                NameSpacedData.Builder().build(),
                authKey.secureArea,
                authKey.alias,
                keyUnlockData,
                ALGORITHM_ES256,
            )
            val data = generator.generate()
            responseGenerator.addDocument(data)
        } catch (lockedException: SecureArea.KeyLockedException) {
            Log.e(this.TAG, "error", lockedException)
            return AddDocumentToResponse.UserAuthRequired(keyUnlockData)
        }
        return AddDocumentToResponse.Success
    }

    /**
     * Closes the connection and clears the data of the session
     *
     * Also, sends a termination message if there is a connected mdoc verifier
     *
     * @param sendSessionTerminationMessage Whether to send session termination message.
     * @param useTransportSpecificSessionTermination Whether to use transport-specific session
     */
    override fun stopPresentation(
        sendSessionTerminationMessage: Boolean,
        useTransportSpecificSessionTermination: Boolean,
    ) {
        deviceRetrievalHelper?.stopPresentation(
            sendSessionTerminationMessage,
            useTransportSpecificSessionTermination,
        )
        disconnect()
    }

    private fun disconnect() {
        qrEngagement?.close()
        destroy()
    }

    private fun destroy() {
        deviceRetrievalHelper = null
        qrEngagement = null
        engagementToApp = null
        hasStarted = false
    }

    private sealed interface AddDocumentToResponse {
        object Success : AddDocumentToResponse
        data class UserAuthRequired(val keyUnlockData: KeyUnlockData) :
            AddDocumentToResponse
    }
}

private fun Collection<TransferEvent.Listener>.onTransferEvent(event: TransferEvent) {
    forEach { it.onTransferEvent(event) }
}
