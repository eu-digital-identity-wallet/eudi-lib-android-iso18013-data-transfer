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
import kotlinx.io.files.Path
import java.io.File

/**
 * Response Generator
 */
interface ResponseGenerator<in RQ : Request, RS : Response> {
    /**
     * Parse the request and extract the requested document data
     *
     * @param request
     * @return [RequestedDocumentData]
     */
    fun parseRequest(request: RQ): RequestedDocumentData

    /**
     * Create a response based on the disclosed documents
     *
     * @param disclosedDocuments
     * @return [ResponseResult]
     */
    fun createResponse(disclosedDocuments: DisclosedDocuments): ResponseResult<RS>

    /**
     * Set the reader trust store.
     * This is used to accomplish the reader authentication.
     *
     * @param readerTrustStore
     */
    fun setReaderTrustStore(readerTrustStore: ReaderTrustStore): ResponseGenerator<RQ, RS>

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

        private val storageEngine: StorageEngine by lazy {
            AndroidStorageEngine.Builder(
                _context,
                Path(File(_context.noBackupFilesDir.path, "eudi-identity.bin").path)
            )
                .setUseEncryption(true)
                .build()
        }

        private val androidSecureArea: AndroidKeystoreSecureArea by lazy {
            AndroidKeystoreSecureArea(_context, storageEngine)
        }
    }
}


