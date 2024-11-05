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
import eu.europa.ec.eudi.iso18013.transfer.engagement.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.Response
import eu.europa.ec.eudi.wallet.document.DocumentManager

/**
 * Transfer manager interface for managing the transfer of data between the wallet and the reader.
 */
interface TransferManager : TransferEvent.Listenable {

    val requestProcessor: RequestProcessor

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
     * @see NfcEngagementService
     */
    fun setupNfcEngagement(service: NfcEngagementService): TransferManager

    /**
     * Starts the QR Engagement and generates the QR code
     * Once the QR code is ready, the event [TransferEvent.QrEngagementReady] will be triggered
     */
    fun startQrEngagement()

    /**
     * Starts the engagement to app, according to ISO 18013-7.
     * @param intent The intent being received
     */
    fun startEngagementToApp(intent: Intent)

    /**
     * Sends response bytes to the connected reader
     * To generate the response, use the [RequestProcessor.ProcessedRequest.Success.generateResponse]
     * method.
     * @param response The response to be sent
     */
    fun sendResponse(response: Response)

    /**
     * Closes the connection and clears the data of the session
     * Also, sends a termination message if there is a connected verifier
     *
     * @param sendSessionTerminationMessage Whether to send session termination message.
     * @param useTransportSpecificSessionTermination Whether to use transport-specific session
     */
    fun stopPresentation(
        sendSessionTerminationMessage: Boolean = true,
        useTransportSpecificSessionTermination: Boolean = false,
    )

    /**
     * Companion object for creating a new instance of [TransferManager]
     */
    companion object {
        /**
         * Create a new instance of [TransferManager] for the ISO 18013-5 and ISO 18013-7
         * standards.
         *
         * @param context
         * @param documentManager
         * @param readerTrustStore
         * @param retrievalMethods
         * @return a [TransferManagerImpl]
         */
        @JvmStatic
        fun getDefault(
            context: Context,
            documentManager: DocumentManager,
            readerTrustStore: ReaderTrustStore? = null,
            retrievalMethods: List<DeviceRetrievalMethod>? = null
        ): TransferManager = TransferManagerImpl(context) {
            documentManager(documentManager)
            readerTrustStore?.let { readerTrustStore(it) }
            retrievalMethods?.let { retrievalMethods(it) }
        }
    }
}
