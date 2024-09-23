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
package eu.europa.ec.eudi.iso18013.transfer

import android.content.Context
import android.content.Intent
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.response.DeviceRequest
import eu.europa.ec.eudi.iso18013.transfer.response.DeviceResponseGeneratorImpl
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseGenerator

/**
 * Transfer manager
 *
 * @constructor Create empty Transfer manager
 */
interface TransferManager : TransferEvent.Listenable {

    val responseGenerator: ResponseGenerator<DeviceRequest>

    /**
     * Set retrieval methods
     *
     * @param retrievalMethods
     * @return a [TransferManager]
     */
    fun setRetrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>): TransferManager

    /**
     * Setup the [NfcEngagementService]
     * Note: This method is only for internal use and should not be called by the app
     * @param service
     */
    fun setupNfcEngagement(service: NfcEngagementService): TransferManager

    /**
     * Starts the QR Engagement and generates the QR code
     *
     * Once the QR code is ready, you will receive the event [TransferEvent.QrEngagementReady]
     */
    fun startQrEngagement()

    /**
     * Starts the engagement to app, according to ISO 18013-7.
     *
     * @param intent The intent being received
     */
    fun startEngagementToApp(intent: Intent)

    /**
     * Send response
     *
     * @param responseBytes
     */
    fun sendResponse(responseBytes: ByteArray)

    /**
     * Closes the connection and clears the data of the session
     *
     * Also, sends a termination message if there is a connected mdoc verifier
     *
     * @param sendSessionTerminationMessage Whether to send session termination message.
     * @param useTransportSpecificSessionTermination Whether to use transport-specific session
     */
    fun stopPresentation(
        sendSessionTerminationMessage: Boolean = true,
        useTransportSpecificSessionTermination: Boolean = false,
    )

    /**
     * Builder class for instantiating a [TransferManager] implementation
     *
     * @constructor
     *
     * @param context
     */
    class Builder(context: Context) {
        private val context = context.applicationContext
        var responseGenerator: ResponseGenerator<DeviceRequest>? = null
        var retrievalMethods: List<DeviceRetrievalMethod>? = null

        /**
         * Response generator that will be parse the request and will create the response
         *
         * @param responseGenerator
         */
        fun responseGenerator(responseGenerator: DeviceResponseGeneratorImpl) =
            apply { this.responseGenerator = responseGenerator }

        /**
         * Retrieval methods that will be used to retrieve the device request from the mdoc verifier
         *
         * @param retrievalMethods
         */
        fun retrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>) =
            apply { this.retrievalMethods = retrievalMethods }

        /**
         * Build a [TransferManager] instance
         *
         * @return [TransferManager]
         */
        fun build(): TransferManager {
            return responseGenerator?.let { responseGenerator ->
                TransferManagerImpl(context, responseGenerator as DeviceResponseGeneratorImpl).apply {
                    retrievalMethods?.let { setRetrievalMethods(it) }
                }
            } ?: throw IllegalArgumentException("responseGenerator not set")
        }
    }
}
