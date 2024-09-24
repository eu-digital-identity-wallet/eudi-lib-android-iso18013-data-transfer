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

import androidx.biometric.BiometricPrompt
import eu.europa.ec.eudi.iso18013.transfer.response.Response

/**
 * Represents the result of a response
 */
sealed interface ResponseResult<out R : Response> {
    /**
     * User authentication is required to proceed with the response generation
     * @property cryptoObject the crypto object
     */
    data class UserAuthRequired<R : Response>(val cryptoObject: BiometricPrompt.CryptoObject?) :
        ResponseResult<R>

    /**
     * The response generation was successful
     * @property response the response
     */
    data class Success<R : Response>(val response: R) : ResponseResult<R>

    /**
     * The response generation failed
     * @property throwable the throwable
     */
    data class Failure<R : Response>(val throwable: Throwable) : ResponseResult<R>
}
