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

package eu.europa.ec.eudi.iso18013.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.security.cert.X509Certificate

/**
 * Represents a Device Request
 *
 * @property docType the document type e.g. eu.europa.ec.eudiw.pid.1
 * @property requestItems a [List] of the requested document elements [DocItem] (namespace and element identifier)
 * @property readerAuth reader authentication [ReaderAuth] object if exist
 * @constructor Create empty Doc request
 */
@Parcelize
class DocRequest(
    val docType: String,
    val requestItems: List<DocItem>,
    val readerAuth: ReaderAuth?
) : Parcelable

/**
 * Doc item represents a data element
 *
 * @property namespace the namespace that the element belong to, e.g. eu.europa.ec.eudiw.pid.1
 * @property elementIdentifier the data element identifier e.g. family_name, given_name
 * @constructor Create empty Doc item
 */
@Parcelize
data class DocItem(
    val namespace: String,
    val elementIdentifier: String,
) : Parcelable

/**
 * Reader authentication
 *
 * @property readerAuth the reader authentication structure as CBOR encoded [ByteArray]
 * @property readerSignIsValid indicates if the signature of reader authentication is valid
 * @property readerCertificateChain reader auth certificate chain as [List] of [X509Certificate]
 * @property readerCertificatedIsTrusted result of reader auth certificate path validation
 * @property readerCommonName the Common Name (CN) field of the reader authentication certificate
 * @constructor Create empty Reader auth
 */
@Parcelize
class ReaderAuth(
    val readerAuth: ByteArray,
    val readerSignIsValid: Boolean,
    val readerCertificateChain: List<X509Certificate>,
    val readerCertificatedIsTrusted: Boolean,
    val readerCommonName: String,
) : Parcelable {
    /**
     * Whether the reader authentication is success (including that the signature of reader authentication is valid and reader auth
     * certificate path is valid)
     *
     * @return a [Boolean] value indicating if reader authentication is success or not
     */
    fun isSuccess(): Boolean {
        return readerSignIsValid &&
            readerCertificatedIsTrusted
    }
}
