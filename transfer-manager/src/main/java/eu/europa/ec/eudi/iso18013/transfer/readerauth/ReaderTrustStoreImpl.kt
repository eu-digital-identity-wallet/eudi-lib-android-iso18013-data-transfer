/*
 *  Copyright (c) 2025 European Commission
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.europa.ec.eudi.iso18013.transfer.readerauth

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.crl.CertificateCRLValidation
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.crl.CertificateCRLValidationException
import eu.europa.ec.eudi.iso18013.transfer.readerauth.profile.ProfileValidation
import org.bouncycastle.asn1.x500.X500Name
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.cert.*
import java.util.*

class ReaderTrustStoreImpl(
    private val trustedCertificates: List<X509Certificate>,
    private val profileValidation: ProfileValidation,
    private var errorLogger: ((tag: String, message: String, cause: Throwable) -> Unit) = { tag, message, cause ->
        Log.d(tag, message, cause)
    }
) : ReaderTrustStore {

    private val trustedCertMap: Map<X500Name, X509Certificate> by lazy {
        trustedCertificates.associateBy { X500Name(it.subjectX500Principal.name) }
    }

    override fun createCertificationTrustPath(chain: List<X509Certificate>): List<X509Certificate>? {
        for (certificate in chain) {
            val x500Name = X500Name(certificate.issuerX500Principal.name)
            trustedCertMap[x500Name]?.let {
                return listOf(certificate, it)
            }
        }
        return null
    }

    /**
     * Validates the certification trust path of a document signer.
     *
     * This function verifies the certificate chain against a set of trusted certificates
     * and performs additional profile validation on the signer's certificate.
     *
     * @param chainToDocumentSigner The certificate chain leading to the document signer's certificate.
     * @return `true` if the certification trust path is valid, `false` otherwise.
     */
    override fun validateCertificationTrustPath(chainToDocumentSigner: List<X509Certificate>): Boolean {
        if (chainToDocumentSigner.isEmpty()) return false

        var result = false

        try {
            val certStore = CertStore.getInstance(
                "Collection",
                CollectionCertStoreParameters(trustedCertificates),
            )
            val trustAnchors = trustedCertificates.map {
                TrustAnchor(it, null)
            }.toSet()

            val params = PKIXParameters(trustAnchors).apply {
                isRevocationEnabled = false
                addCertStore(certStore)
                date = Date()
            }
            val certificateFactory =
                CertificateFactory.getInstance("X.509")
            val certPath = certificateFactory.generateCertPath(chainToDocumentSigner)
            val certPathValidationResult = CertPathValidator
                .getInstance("PKIX")
                .validate(certPath, params) as PKIXCertPathValidatorResult
            val trustedRootCA = certPathValidationResult.trustAnchor.trustedCert
            result = profileValidation.validate(chainToDocumentSigner, trustedRootCA)
            chainToDocumentSigner.first().let { certificate ->
                CertificateCRLValidation.verify(certificate)
            }

        } catch (e: Throwable) {
            when (e) {
                is InvalidAlgorithmParameterException ->
                    errorLogger(TAG, "INVALID_ALGORITHM_PARAMETER", e)

                is NoSuchAlgorithmException -> errorLogger(TAG, "NO_SUCH_ALGORITHM", e)
                is CertificateException -> errorLogger(TAG, "CERTIFICATE_ERROR", e)
                is CertPathValidatorException -> errorLogger(TAG, "CERTIFICATE_PATH_ERROR", e)
                is CertificateCRLValidationException -> errorLogger(TAG, "CERTIFICATE_REVOKED", e)
                else -> errorLogger(TAG, "UNKNOWN_ERROR", e)
            }
        }

        return result
    }

    companion object {
        private const val TAG = "ReaderTrustStoreImpl"
    }
}