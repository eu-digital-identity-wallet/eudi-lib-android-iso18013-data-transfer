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

package eu.europa.ec.eudi.iso18013.transfer.response

import com.android.identity.crypto.Algorithm

/**
 * Interface for request processor. A request processor processes the raw request and returns a processed request.
 * The processed request can be either a success or a failure.
 * If the processed request is a success, the requested documents are returned and the response can be generated.
 * If the processed request is a failure, the error is returned.
 *
 * @property readerTrustStore the reader trust store to be used for performing reader authentication
 */
fun interface RequestProcessor {

    /**
     * Processes the request
     * @param request the request
     * @return the processed request
     */
    fun process(request: Request): ProcessedRequest

    /**
     * Represents the result of a processed request
     */
    sealed interface ProcessedRequest {
        /**
         * The request processing was successful.
         *
         * @property requestedDocuments the requested documents
         */
        abstract class Success(val requestedDocuments: RequestedDocuments) : ProcessedRequest {
            /**
             * Generates the response for the disclosed documents
             * @param disclosedDocuments the disclosed documents
             * @param signatureAlgorithm the signature algorithm
             * @return the response result containing the response or the error
             */
            abstract fun generateResponse(
                disclosedDocuments: DisclosedDocuments,
                signatureAlgorithm: Algorithm?,
            ): ResponseResult
        }

        /**
         * The request processing failed
         * @property error the error
         */
        data class Failure(val error: Throwable) : ProcessedRequest

        /**
         * Returns the processed request or throws the error
         * @throws Throwable the error
         * @return the processed request
         */
        fun getOrThrow(): ProcessedRequest {
            return when (this) {
                is Success -> this
                is Failure -> throw error
            }
        }

        /**
         * Returns the processed request or null
         * @return the processed request or null
         */
        fun getOrNull(): ProcessedRequest? {
            return when (this) {
                is Success -> this
                is Failure -> null
            }
        }
    }
}