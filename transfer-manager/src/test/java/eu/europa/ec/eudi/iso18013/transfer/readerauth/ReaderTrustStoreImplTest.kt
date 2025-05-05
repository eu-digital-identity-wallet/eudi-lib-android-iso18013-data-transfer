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
import eu.europa.ec.eudi.iso18013.transfer.mockAndroidLog
import eu.europa.ec.eudi.iso18013.transfer.readerauth.profile.ProfileValidation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import java.security.Security
import java.security.cert.X509Certificate
import kotlin.test.assertTrue

class ReaderTrustStoreImplTest {

    private lateinit var mockLog: MockedStatic<Log>
    private lateinit var mockCrlValidation: MockedStatic<CertificateCRLValidation>
    private lateinit var rootCertificate: X509Certificate
    private lateinit var leafCertificate: X509Certificate

    @Before
    fun setup() {
        Security.addProvider(BouncyCastleProvider())
        mockLog = mockAndroidLog()

        // Create root and leaf certificates for tests
        createCertificates()

        // Mock the CRL validation
        mockCrlValidation = Mockito.mockStatic(CertificateCRLValidation::class.java)
        mockCrlValidation.`when`<Unit> { CertificateCRLValidation.verify(any()) }.thenAnswer { }
    }

    private fun createCertificates() {
        rootCertificate = trustedCertificate
        leafCertificate = validCertificate
    }

    @After
    fun close() {
        mockLog.close()
        mockCrlValidation.close()
        // clear mockStatic
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun testChain() {
        // Create certificate chain with leaf certificate first, followed by root
        val certificateChain = listOf(leafCertificate, rootCertificate)

        // For debugging: output certificate information
        println("Leaf certificate subject: ${leafCertificate.subjectX500Principal.name}")
        println("Leaf certificate issuer: ${leafCertificate.issuerX500Principal.name}")
        println("Root certificate subject: ${rootCertificate.subjectX500Principal.name}")

        // Create a custom trust store with mocked ProfileValidation
        val profileValidation = mockk<ProfileValidation>()
        every { profileValidation.validate(any(), any()) } returns true

        // Setup trust store with rootCA as the trusted certificate
        val trustStore = ReaderTrustStoreImpl(
            trustedCertificates = listOf(rootCertificate),
            profileValidation = profileValidation,
            // Add a custom error logger to help with debugging
            errorLogger = { tag, message, cause ->
                println("$tag: $message")
                cause?.printStackTrace()
            }
        )

        // Use createCertificationTrustPath to debug
        val trustPath = trustStore.createCertificationTrustPath(certificateChain)
        println("Trust path created: ${trustPath != null}")

        // Validate the certificate chain with the trust store
        val result = trustStore.validateCertificationTrustPath(certificateChain)

        // Verify the chain is valid
        assertTrue(result, "Certificate chain validation should succeed")

        // Verify that profile validation was called
        verify(exactly = 1) {
            profileValidation.validate(any(), any())
        }
    }

    @Test
    fun testCRLValidationIsPerformedForLeafCertificate() {
        // Create certificate chain with leaf certificate first, followed by root
        val certificateChain = listOf(leafCertificate, rootCertificate)

        // Create a mock ProfileValidation that always returns true
        val profileValidation = mockk<ProfileValidation>()
        every { profileValidation.validate(any(), any()) } returns true

        // Setup trust store with rootCA as the trusted certificate
        val trustStore = ReaderTrustStoreImpl(
            trustedCertificates = listOf(rootCertificate),
            profileValidation = profileValidation
        )

        // Validate the certificate chain
        val result = trustStore.validateCertificationTrustPath(certificateChain)

        // Verify the chain is valid
        assertTrue(result, "Certificate chain validation should succeed")

        // Verify that CRL validation was performed exactly once for the leaf certificate
        mockCrlValidation.verify({ CertificateCRLValidation.verify(eq(leafCertificate)) }, times(1))

        // Verify that CRL validation was NOT performed for the root certificate
        mockCrlValidation.verify({ CertificateCRLValidation.verify(eq(rootCertificate)) }, times(0))
    }
}
