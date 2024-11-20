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

package eu.europa.ec.eudi.iso18013.transfer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.android.identity.android.mdoc.deviceretrieval.DeviceRetrievalHelper
import com.android.identity.mdoc.origininfo.OriginInfo
import com.android.identity.mdoc.origininfo.OriginInfoDomain
import com.android.identity.util.Constants
import eu.europa.ec.eudi.iso18013.transfer.engagement.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.internal.EngagementToApp
import eu.europa.ec.eudi.iso18013.transfer.internal.QrEngagement
import eu.europa.ec.eudi.iso18013.transfer.internal.TAG
import eu.europa.ec.eudi.iso18013.transfer.internal.stopPresentation
import eu.europa.ec.eudi.iso18013.transfer.internal.transportOptions
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.device.DeviceRequest
import eu.europa.ec.eudi.iso18013.transfer.response.device.DeviceRequestProcessor
import eu.europa.ec.eudi.wallet.document.DocumentManager

/**
 * Transfer Manager class used for performing device engagement and data retrieval
 * for ISO 18013-5 and ISO 18013-7 standards.
 *
 * @property retrievalMethods list of device retrieval methods to be used for the transfer
 *
 * @constructor Create a Transfer Manager
 * @param context application context
 * @param requestProcessor request processor for processing the device request and generating the response
 * @param retrievalMethods list of device retrieval methods to be used for the transfer
 */
class TransferManagerImpl @JvmOverloads constructor(
    context: Context,
    override val requestProcessor: RequestProcessor,
    retrievalMethods: List<DeviceRetrievalMethod>? = null,
) : TransferManager {
    private val context = context.applicationContext

    /**
     * Device retrieval helper instance
     */
    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var deviceRetrievalHelper: DeviceRetrievalHelper? = null

    /**
     * Engagement to app instance
     */
    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var engagementToApp: EngagementToApp? = null

    /**
     * QR engagement instance
     */
    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var qrEngagement: QrEngagement? = null

    /**
     * Flag to check if the transfer has started
     */
    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var hasStarted = false

    /**
     * List of device retrieval methods
     */
    var retrievalMethods: List<DeviceRetrievalMethod> =
        retrievalMethods?.let { listOf(*it.toTypedArray()) } ?: emptyList()
        private set(value) {
            field = listOf(*value.toTypedArray())
        }

    /**
     * List of transfer event listeners
     */
    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val transferEventListeners = mutableListOf<TransferEvent.Listener>()

    /**
     * Add a transfer event listener
     * @param listener a transfer event listener
     * @return instance of [TransferManager]
     */
    override fun addTransferEventListener(listener: TransferEvent.Listener) = apply {
        transferEventListeners.add(listener)
    }

    /**
     * Remove a transfer event listener
     * @param listener a transfer event listener
     * @return instance of [TransferManager]
     */
    override fun removeTransferEventListener(listener: TransferEvent.Listener) = apply {
        transferEventListeners.remove(listener)
    }

    /**
     * Remove all transfer event listeners
     * @return instance of [TransferManager]
     */
    override fun removeAllTransferEventListeners() = apply {
        transferEventListeners.clear()
    }

    /**
     * Set retrieval methods
     * @param retrievalMethods list of device retrieval methods
     * @return instance of [TransferManager]
     */
    override fun setRetrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>) = apply {
        this.retrievalMethods = retrievalMethods
    }

    /**
     * Starts the QR Engagement and generates the QR code
     * Once the QR code is ready, the event [TransferEvent.QrEngagementReady] will be triggered
     * If the transfer has already started, an error event will be triggered
     * with an [IllegalStateException] containing the message "Transfer has already started."
     * @see TransferEvent.QrEngagementReady
     * @see TransferEvent.Error
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
            onQrEngagementReady = { qrCode ->
                transferEventListeners.onTransferEvent(
                    TransferEvent.QrEngagementReady(qrCode),
                )
            },
            onConnecting = {
                transferEventListeners.onTransferEvent(TransferEvent.Connecting)
            },
            onDeviceRetrievalHelperReady = { deviceRetrievalHelper ->
                this.deviceRetrievalHelper = deviceRetrievalHelper
                transferEventListeners.onTransferEvent(TransferEvent.Connected)
            },
            onNewDeviceRequest = { deviceRequestBytes ->
                val deviceRequest = DeviceRequest(
                    deviceRequestBytes,
                    deviceRetrievalHelper?.sessionTranscript!!
                )
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        processedRequest = requestProcessor.process(deviceRequest),
                        request = deviceRequest
                    )
                )
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

    /**
     * Sets up NFC engagement with the provided service
     * Note: This method is only for internal use and should not be called by the app
     * @param service the NFC engagement service
     * @return instance of [TransferManager]
     */
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
                val deviceRequest = DeviceRequest(
                    deviceRequestBytes,
                    deviceRetrievalHelper?.sessionTranscript!!
                )
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        processedRequest = requestProcessor.process(deviceRequest),
                        request = deviceRequest
                    )
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
     * @param intent The intent being received
     * If the transfer has already started, an error event will be triggered
     * with an [IllegalStateException] containing the message "Transfer has already started."
     * @see TransferEvent.Error
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
            originInfos.add(OriginInfoDomain(mdocReferrerUri))
        }

        engagementToApp = EngagementToApp(
            context = context,
            dataTransportOptions = retrievalMethods.transportOptions,
            onPresentationReady = { deviceRetrievalHelper ->
                this.deviceRetrievalHelper = deviceRetrievalHelper
                transferEventListeners.onTransferEvent(TransferEvent.Connected)
            },
            onNewRequest = { deviceRequestBytes ->
                val deviceRequest = DeviceRequest(
                    deviceRequestBytes,
                    deviceRetrievalHelper?.sessionTranscript!!
                )
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        processedRequest = requestProcessor.process(deviceRequest),
                        request = deviceRequest
                    )
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

    /**
     * Sends the response bytes to the connected mdoc verifier
     * To generate the response bytes, use the [RequestProcessor.ProcessedRequest.Success.generateResponse]
     * from the TransferManagerImpl.requestProcessor
     * @param responseBytes the response bytes to be sent
     */
    override fun sendResponse(responseBytes: ByteArray) {
        deviceRetrievalHelper?.sendDeviceResponse(
            responseBytes,
            Constants.SESSION_DATA_STATUS_SESSION_TERMINATION
        )
        transferEventListeners.onTransferEvent(TransferEvent.ResponseSent)
    }

    /**
     * Closes the connection and clears the data of the session
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

    /**
     * Disconnects the current session and clears the engagement objects
     */
    private fun disconnect() {
        qrEngagement?.close()
        destroy()
    }

    /**
     * Destroys the current session and clears the engagement objects
     */
    private fun destroy() {
        deviceRetrievalHelper = null
        qrEngagement = null
        engagementToApp = null
        hasStarted = false
    }

    /**
     * Companion object for creating a new instance of [TransferManager]
     */
    companion object {
        private const val TRANSFER_STARTED_MSG = "Transfer has already started."

        /**
         * Extension function to trigger transfer events for each listener in the collection
         *
         * @param event the transfer event to be triggered
         */
        private fun Collection<TransferEvent.Listener>.onTransferEvent(event: TransferEvent) {
            forEach { it.onTransferEvent(event) }
        }

        /**
         * Create a new instance of [TransferManager]
         */
        operator fun invoke(context: Context, builder: Builder.() -> Unit): TransferManagerImpl {
            val builder = Builder(context).apply(builder)
            return builder.build()
        }
    }

    /**
     * Builder class for instantiating a [TransferManager] implementation
     *
     * @property documentManager document manager instance
     * @property readerTrustStore reader trust store instance
     * @property retrievalMethods list of device retrieval methods
     * @constructor
     * @param context
     */
    class Builder(context: Context) {
        private val context = context.applicationContext
        var documentManager: DocumentManager? = null
        var readerTrustStore: ReaderTrustStore? = null
        var retrievalMethods: List<DeviceRetrievalMethod>? = null

        /**
         * Document manager instance that will be used to retrieve the requested documents
         * @param documentManager
         */
        fun documentManager(documentManager: DocumentManager) = apply {
            this.documentManager = documentManager
        }

        /**
         * Reader trust store instance that will be used to verify the reader's certificate
         * @param readerTrustStore
         */
        fun readerTrustStore(readerTrustStore: ReaderTrustStore) = apply {
            this.readerTrustStore = readerTrustStore
        }

        /**
         * Retrieval methods that will be used to retrieve the device request from the mdoc verifier
         * @param retrievalMethods
         */
        fun retrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>) =
            apply { this.retrievalMethods = retrievalMethods }

        /**
         * Build a [eu.europa.ec.eudi.iso18013.transfer.TransferManagerImpl] instance
         * with [DeviceRequestProcessor] instance
         * @return [eu.europa.ec.eudi.iso18013.transfer.TransferManagerImpl]
         */
        fun build(): TransferManagerImpl {
            requireNotNull(documentManager) { "Document manager must be provided" }
            return TransferManagerImpl(
                context = context,
                requestProcessor = DeviceRequestProcessor(
                    documentManager = documentManager!!,
                    readerTrustStore = readerTrustStore
                ),
                retrievalMethods = retrievalMethods ?: emptyList()
            )
        }
    }
}