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

package eu.europa.ec.eudi.iso18013.transfer.internal.readerauth

import com.android.identity.crypto.javaX509Certificates
import com.android.identity.mdoc.request.DeviceRequestParser
import eu.europa.ec.eudi.iso18013.transfer.internal.cn
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuth

internal fun ReaderTrustStore.performReaderAuthentication(docRequest: DeviceRequestParser.DocRequest): ReaderAuth? {
    val readerAuth = docRequest.readerAuth ?: return null
    val readerCertificateChain = docRequest.readerCertificateChain ?: return null

    if (docRequest.readerCertificateChain?.javaX509Certificates?.isEmpty() == true) return null

    val certChain = this
        .createCertificationTrustPath(readerCertificateChain.javaX509Certificates)
        ?.takeIf { it.isNotEmpty() }
        ?: readerCertificateChain.javaX509Certificates

    return ReaderAuth(
        readerAuth,
        docRequest.readerAuthenticated,
        readerCertificateChain.javaX509Certificates,
        validateCertificationTrustPath(readerCertificateChain.javaX509Certificates),
        certChain.cn
    )
}