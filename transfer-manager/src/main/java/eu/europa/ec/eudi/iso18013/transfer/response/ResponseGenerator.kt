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

@file:JvmMultifileClass

package eu.europa.ec.eudi.iso18013.transfer.response

import android.content.Context
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.DocumentsResolver
import eu.europa.ec.eudi.iso18013.transfer.RequestedDocumentData
import eu.europa.ec.eudi.iso18013.transfer.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore

/**
 * Response Generator
 */
abstract class ResponseGenerator<T> {
    abstract fun parseRequest(request: T): RequestedDocumentData
    abstract fun createResponse(
        disclosedDocuments: DisclosedDocuments
    ): ResponseResult

    abstract fun setReaderTrustStore(readerTrustStore: ReaderTrustStore): ResponseGenerator<T>

    /**
     * Builder class for instantiating a [ResponseGenerator] implementation
     *
     * @constructor
     *
     * @param context
     */
    class Builder(context: Context) {
        private val _context = context.applicationContext
        var documentsResolver: DocumentsResolver? = null
        var readerTrustStore: ReaderTrustStore? = null

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
         * Build a [ResponseGenerator] instance
         *
         * @return [ResponseGenerator]
         */
        fun build(): DeviceResponseGeneratorImpl {
            return documentsResolver?.let { documentsResolver ->
                DeviceResponseGeneratorImpl(
                    _context,
                    documentsResolver,
                    storageEngine,
                    androidSecureArea
                ).apply {
                    readerTrustStore?.let { setReaderTrustStore(it) }
                }
            } ?: throw IllegalArgumentException("documentResolver not set")
        }

        private val storageEngine: StorageEngine
            get() = AndroidStorageEngine.Builder(_context, _context.noBackupFilesDir)
                .setUseEncryption(true)
                .build()
        private val androidSecureArea: AndroidKeystoreSecureArea
            get() = AndroidKeystoreSecureArea(_context, storageEngine)
    }
}

interface Response
typealias DeviceResponseBytes = ByteArray
class DeviceResponse(val deviceResponseBytes: DeviceResponseBytes): Response

interface Request
typealias SessionTranscriptBytes = ByteArray
typealias DeviceRequestBytes = ByteArray
class DeviceRequest(val deviceRequestBytes: DeviceRequestBytes,
                    val sessionTranscriptBytes: SessionTranscriptBytes): Request