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

import eu.europa.ec.eudi.iso18013.transfer.internal.cn
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuth
import kotlinx.coroutines.runBlocking
import org.multipaz.cbor.Cbor
import org.multipaz.cbor.DataItem
import org.multipaz.cose.Cose
import org.multipaz.cose.toCoseLabel
import org.multipaz.crypto.SignatureVerificationException
import org.multipaz.crypto.javaX509Certificates
import org.multipaz.mdoc.request.DocRequest
import org.multipaz.mdoc.request.DeviceRequest as MultipazDeviceRequest

/**
 * Extract reader authentication information from a DocRequest and verify signature.
 *
 * In multipaz 0.95, reader authentication signature is verified at the DeviceRequest level
 * via verifyReaderAuthentication(). This method:
 * 1. Verifies the signature using the parsedRequest and sessionTranscript
 * 2. Extracts the reader certificate chain
 * 3. Validates trust against the ReaderTrustStore
 *
 * @param docRequest The document request containing reader authentication info
 * @param parsedRequest The parsed DeviceRequest for signature verification
 * @param sessionTranscript The session transcript DataItem for verification
 * @return ReaderAuth if reader authentication is present, null otherwise
 */
internal fun ReaderTrustStore.performReaderAuthentication(
    docRequest: DocRequest,
    parsedRequest: MultipazDeviceRequest,
    sessionTranscript: DataItem
): ReaderAuth? {

    val isSignatureValid = try {
        runBlocking {
            parsedRequest.verifyReaderAuthentication(sessionTranscript)
        }
        true
    } catch (_: SignatureVerificationException) {
        false
    }
    // Get reader authentication from DocRequest
    val readerAuth = docRequest.readerAuth ?: return null
    // Get raw COSE bytes for the ReaderAuth field
    val readerAuthBytes = Cbor.encode(readerAuth.toDataItem())

    // Extract certificate chain from COSE headers (prefer protected, fallback to unprotected)
    val certChainDataItem = readerAuth.protectedHeaders[Cose.COSE_LABEL_X5CHAIN.toCoseLabel]
        ?: readerAuth.unprotectedHeaders[Cose.COSE_LABEL_X5CHAIN.toCoseLabel]

    val certChain = certChainDataItem?.asX509CertChain ?: return null
    val certificates = certChain.javaX509Certificates

    if (certificates.isEmpty()) return null

    return ReaderAuth(
        readerAuth = readerAuthBytes,
        readerSignIsValid = isSignatureValid,
        readerCertificateChain = certificates,
        readerCertificatedIsTrusted = validateCertificationTrustPath(certificates),
        readerCommonName = certificates.cn
    )
}