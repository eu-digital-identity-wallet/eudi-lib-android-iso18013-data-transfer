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
package eu.europa.ec.eudi.iso18013.transfer

import android.content.Context
import android.content.Intent
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService
import eu.europa.ec.eudi.iso18013.transfer.internal.TransferManagerImpl
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore

/**
 * Transfer manager
 *
 * @constructor Create empty Transfer manager
 */
interface TransferManager : TransferEvent.Listenable {

    /**
     * Set retrieval methods
     *
     * @param retrievalMethods
     * @return a [TransferManager]
     */
    fun setRetrievalMethods(retrievalMethods: List<DeviceRetrievalMethod>): TransferManager

    /**
     * Set reader trust store
     *
     * @param readerTrustStore
     * @return a [TransferManager]
     */
    fun setReaderTrustStore(readerTrustStore: ReaderTrustStore): TransferManager

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
     * Create response
     *
     * @param disclosedDocuments as list of [DisclosedDocument]
     * @return a [ResponseResult]
     */
    @Throws(IllegalStateException::class)
    fun createResponse(disclosedDocuments: DisclosedDocuments): ResponseResult

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
        var documentsResolver: DocumentsResolver? = null
        var readerTrustStore: ReaderTrustStore? = null
        var retrievalMethods: List<DeviceRetrievalMethod>? = null

        /**
         * Document resolver that will be used to resolve the documents for the selective disclosure
         * when creating the response
         *
         * @param documentsResolver
         */
        fun documentResolver(documentsResolver: DocumentsResolver) =
            apply { this.documentsResolver = documentsResolver }

        /**
         * Reader trust store that will be used to validate the certificate chain of the mdoc verifier
         *
         * @param readerTrustStore
         */
        fun readerTrustStore(readerTrustStore: ReaderTrustStore) =
            apply { this.readerTrustStore = readerTrustStore }

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
            return documentsResolver?.let { documentsResolver ->
                TransferManagerImpl(context, documentsResolver, storageEngine, androidSecureArea).apply {
                    readerTrustStore?.let { setReaderTrustStore(it) }
                    retrievalMethods?.let { setRetrievalMethods(it) }
                }
            } ?: throw IllegalArgumentException("documentResolver not set")
        }

        private val storageEngine: StorageEngine
            get() = AndroidStorageEngine.Builder(context, context.noBackupFilesDir)
                .setUseEncryption(true)
                .build()

        private val androidSecureArea: AndroidKeystoreSecureArea
            get() = AndroidKeystoreSecureArea(context, storageEngine)
    }
}
