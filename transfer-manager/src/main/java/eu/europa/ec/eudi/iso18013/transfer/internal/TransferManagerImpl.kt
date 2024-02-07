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
import com.android.identity.mdoc.origininfo.OriginInfo
import com.android.identity.mdoc.origininfo.OriginInfoReferrerUrl
import com.android.identity.util.Constants
import eu.europa.ec.eudi.iso18013.transfer.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.TransferEvent
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.engagement.QrCode
import eu.europa.ec.eudi.iso18013.transfer.response.DeviceRequest
import eu.europa.ec.eudi.iso18013.transfer.response.DeviceResponseGeneratorImpl
import java.util.OptionalLong

private const val TRANSFER_STARTED_MSG = "Transfer has already started."

/**
 * Transfer Manager class used for performing device engagement and data retrieval.
 *
 * @param context application context
 * @param responseGenerator response generator instance that parses the request and creates the response
 * @constructor Create empty Transfer manager
 */
internal class TransferManagerImpl(
    context: Context,
    override val responseGenerator: DeviceResponseGeneratorImpl
) : TransferManager {
    private val context = context.applicationContext

    private var deviceRetrievalHelper: DeviceRetrievalHelper? = null

    private var engagementToApp: EngagementToApp? = null
    private var qrEngagement: QrEngagement? = null
    private var hasStarted = false

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
                val deviceRequest = DeviceRequest(
                    deviceRequestBytes,
                    deviceRetrievalHelper?.sessionTranscript!!
                )
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        responseGenerator.parseRequest(
                            deviceRequest
                        ), deviceRequest
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
                        responseGenerator.parseRequest(
                            deviceRequest
                        ), deviceRequest
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
                val deviceRequest = DeviceRequest(
                    deviceRequestBytes,
                    deviceRetrievalHelper?.sessionTranscript!!
                )
                transferEventListeners.onTransferEvent(
                    TransferEvent.RequestReceived(
                        responseGenerator.parseRequest(
                            deviceRequest
                        ), deviceRequest
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
}

private fun Collection<TransferEvent.Listener>.onTransferEvent(event: TransferEvent) {
    forEach { it.onTransferEvent(event) }
}
