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
import android.util.Log
import com.android.identity.android.mdoc.deviceretrieval.DeviceRetrievalHelper
import com.android.identity.android.mdoc.engagement.QrEngagementHelper
import com.android.identity.android.mdoc.transport.DataTransport
import com.android.identity.internal.Util
import com.android.identity.securearea.SecureArea
import eu.europa.ec.eudi.iso18013.transfer.DeviceRetrievalMethod
import java.security.PublicKey

/**
 * Qr engagement
 *
 * @property context context instance
 * @property onConnecting callback to notify that a new mdoc verifier is connecting
 * @property onQrEngagementReady callback to notify that a the QR Engagement is ready
 * @property onDeviceRetrievalHelperReady callback to notify that the device retrieval helper is ready
 * @property onNewDeviceRequest callback to notify that a device request has been received
 * @property onDisconnected callback to notify that the mdoc verifier has been disconnected
 * @property onCommunicationError callback to notify that an error has been occurred
 * @constructor Create empty Qr engagement
 */
internal class QrEngagement(
    private val context: Context,
    private val retrievalMethods: List<DeviceRetrievalMethod>,
    private val onConnecting: () -> Unit,
    private val onQrEngagementReady: () -> Unit,
    private val onDeviceRetrievalHelperReady: (deviceRetrievalHelper: DeviceRetrievalHelper) -> Unit,
    private val onNewDeviceRequest: (request: ByteArray) -> Unit,
    private val onDisconnected: (transportSpecificTermination: Boolean) -> Unit,
    private val onCommunicationError: (error: Throwable) -> Unit,
) {

    private var deviceRetrievalHelper: DeviceRetrievalHelper? = null

    private val eDeviceKeyPair by lazy {
        Util.createEphemeralKeyPair(SecureArea.EC_CURVE_P256)
    }

    private val qrEngagementListener = object : QrEngagementHelper.Listener {

        override fun onDeviceEngagementReady() {
            Log.d(this.TAG, "QR Engagement: Device Engagement Ready")
            onQrEngagementReady()
        }

        override fun onDeviceConnecting() {
            Log.d(this.TAG, "QR Engagement: Device Connecting")
            onConnecting()
        }

        override fun onDeviceConnected(transport: DataTransport) {
            if (deviceRetrievalHelper != null) {
                Log.d(this.TAG, "OnDeviceConnected for QR engagement -> ignoring due to active presentation")
                return
            }

            Log.d(this.TAG, "OnDeviceConnected via QR: qrEngagement=$qrEngagement")

            val builder = DeviceRetrievalHelper.Builder(
                context,
                deviceRetrievalHelperListener,
                context.mainExecutor(),
                eDeviceKeyPair,
            )
            builder.useForwardEngagement(
                transport,
                qrEngagement.deviceEngagement,
                qrEngagement.handover,
            )
            deviceRetrievalHelper = builder.build()
            qrEngagement.close()
            onDeviceRetrievalHelperReady(requireNotNull(deviceRetrievalHelper))
        }

        override fun onError(error: Throwable) {
            Log.d(this.TAG, "QR onError: ${error.message}")
            onCommunicationError(error)
        }
    }

    private val deviceRetrievalHelperListener = object : DeviceRetrievalHelper.Listener {
        override fun onEReaderKeyReceived(p0: PublicKey) {
            Log.d(this.TAG, "DeviceRetrievalHelper Listener (NFC): OnEReaderKeyReceived")
        }

        override fun onDeviceRequest(deviceRequestBytes: ByteArray) {
            Log.d(this.TAG, "DeviceRetrievalHelper Listener (QR): OnDeviceRequest")
            onNewDeviceRequest(deviceRequestBytes)
        }

        override fun onDeviceDisconnected(transportSpecificTermination: Boolean) {
            Log.d(this.TAG, "DeviceRetrievalHelper Listener (QR): onDeviceDisconnected")
            onDisconnected(transportSpecificTermination)
        }

        override fun onError(error: Throwable) {
            Log.d(this.TAG, "DeviceRetrievalHelper Listener (QR): onError -> ${error.message}")
            onCommunicationError(error)
        }
    }

    private lateinit var qrEngagement: QrEngagementHelper

    val deviceEngagementUriEncoded: String
        get() = qrEngagement.deviceEngagementUriEncoded

    /**
     * Configuration of the QR Engagement
     *
     */
    fun configure() {
        qrEngagement = QrEngagementHelper.Builder(
            context,
            eDeviceKeyPair.public,
            retrievalMethods.transportOptions,
            qrEngagementListener,
            context.mainExecutor(),
        ).setConnectionMethods(retrievalMethods.connectionMethods)
            .build()
    }

    /**
     * Closes the connection with the mdoc verifier
     *
     */
    fun close() {
        try {
            qrEngagement.close()
        } catch (exception: RuntimeException) {
            Log.e(this.TAG, "Error closing QR engagement", exception)
        }
    }
}
