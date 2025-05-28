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
package eu.europa.ec.eudi.iso18013.transfer.internal

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.android.identity.android.mdoc.deviceretrieval.DeviceRetrievalHelper
import com.android.identity.android.mdoc.transport.DataTransport
import com.android.identity.android.mdoc.transport.DataTransportOptions
import org.multipaz.crypto.Crypto
import org.multipaz.crypto.EcCurve
import org.multipaz.crypto.EcPublicKey
import org.multipaz.mdoc.engagement.EngagementParser
import org.multipaz.mdoc.origininfo.OriginInfo
import androidx.core.net.toUri

/**
 * Set up Engagement-to-app engagement transmission technology according to the ISO 18013-7
 *
 * To enable 18013-7 REST API functionality, declare to your app's manifest file (AndroidManifest.xml) an Intent Filter for your MainActivity:
 *
 * ```
 * <intent-filter>
 *   <action android:name="android.intent.action.VIEW" />
 *   <category android:name="android.intent.category.DEFAULT" />
 *   <category android:name="android.intent.category.BROWSABLE" />
 *   <data android:scheme="mdoc" android:host="*" />
 * </intent-filter>
 * ```
 *
 * and set launchMode="singleTask" for this activity.
 *
 * In onResume and onNewIntent methods of your MainActivity call tranferManager.startEngagementToApp(<intent>), as following:
 *
 * ```
 * override fun onResume() {
 *   super.onResume()
 *   handleIntent(getIntent())
 * }
 *
 * override fun onNewIntent(intent: Intent?) {
 *   super.onNewIntent(intent)
 *   log("New intent on Activity $intent")
 *   handleIntent(intent)
 * }
 *
 * // To invoke the mdoc App through the mdoc scheme (mdoc://)
 * private fun handleIntent(intent: Intent?) {
 *   if (intent == null || intent.data == null) return
 *   // clear intent after use
 *   setIntent(null)
 *   transferManager.startEngagementToApp(intent)
 *   findNavController(R.id.nav_host_fragment).navigate(
 *   R.id.TransferFragment,
 *   bundleOf(TransferFragment.CLOSE_AFTER_RESPONSE_KEY to true)
 *   )
 * }
 * ```
 *
 * @property context context instance
 * @property onPresentationReady callback to notify that the presentation is ready
 * @property onNewRequest callback to notify that a request has been received
 * @property onDisconnected callback to notify that the mdoc verifier has been disconnected
 * @property onCommunicationError callback to notify that an error has been occurred
 * @constructor Create empty Engagement to app setup
 */
internal class EngagementToApp(
    private val context: Context,
    private val dataTransportOptions: DataTransportOptions,
    private val onPresentationReady: (deviceRetrievalHelper: DeviceRetrievalHelper) -> Unit,
    private val onNewRequest: (request: ByteArray) -> Unit,
    private val onDisconnected: () -> Unit,
    private val onCommunicationError: (error: Throwable) -> Unit,
) {

    @JvmSynthetic
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val eDevicePrivateKey = Crypto.createEcPrivateKey(EcCurve.P256)

    private val presentationListener = object : DeviceRetrievalHelper.Listener {
        override fun onEReaderKeyReceived(eReaderKey: EcPublicKey) {
            Log.d(TAG, "DeviceRetrievalHelper Listener (QR): OnEReaderKeyReceived")
        }

        override fun onDeviceRequest(deviceRequestBytes: ByteArray) {
            onNewRequest(deviceRequestBytes)
        }

        override fun onDeviceDisconnected(transportSpecificTermination: Boolean) {
            onDisconnected()
        }

        override fun onError(error: Throwable) {
            onCommunicationError(error)
        }
    }

    /**
     * Configuration of the Engagement to app
     *
     * @param reverseEngagementUri the URI shall start with “mdoc://”
     * @param origins a [List] of [OriginInfo] that define the engagement channel
     */
    fun configure(
        reverseEngagementUri: String,
        origins: List<OriginInfo>,
    ) {
        val uri = reverseEngagementUri.toUri()
        check(uri.scheme.equals("mdoc")) { "Only supports mdoc URIs" }

        val encodedReaderEngagement = Base64.decode(
            uri.encodedSchemeSpecificPart,
            Base64.URL_SAFE or Base64.NO_PADDING,
        )
        val engagement = EngagementParser(encodedReaderEngagement).parse()
        check(engagement.connectionMethods.isNotEmpty()) { "No connection methods in engagement" }

        // For now, just pick the first transport
        val connectionMethod = engagement.connectionMethods[0]
        Log.d(this.TAG, "Using connection method $connectionMethod")

        val transport = DataTransport.fromConnectionMethod(
            context,
            connectionMethod,
            DataTransport.Role.MDOC,
            dataTransportOptions,
        )

        val builder = DeviceRetrievalHelper.Builder(
            context,
            presentationListener,
            context.mainExecutor(),
            eDevicePrivateKey,
        ).useReverseEngagement(transport, encodedReaderEngagement, origins)
        builder.build().apply {
            onPresentationReady(this)
        }
    }
}
