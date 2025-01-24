/*
 * Copyright (c) 2025 European Commission
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

package eu.europa.ec.eudi.iso18013.transfer.readerauth

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.crl.CertificateCRLValidation
import eu.europa.ec.eudi.iso18013.transfer.mockAndroidLog
import eu.europa.ec.eudi.iso18013.transfer.readerauth.profile.ProfileValidation
import io.mockk.spyk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Base64
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReaderTrustStoreImplTest {

    private lateinit var readerTrustStore: ReaderTrustStoreImpl
    private lateinit var trustedCertificates: List<X509Certificate>

    private lateinit var mockLog: MockedStatic<Log>

    @Before
    fun setup() {
        trustedCertificates = listOf(loadTrustCert())
        readerTrustStore =
            ReaderTrustStoreImpl(trustedCertificates, ProfileValidation.Companion.DEFAULT)

        mockLog = mockAndroidLog()

        // Mock the CRL validation
        Mockito.mockStatic(CertificateCRLValidation::class.java).apply {
            Mockito.`when`(CertificateCRLValidation.verify(any())).thenAnswer { }
        }
    }

    @After
    fun close() {
        mockLog.close()
        // clear mockStatic
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun testCreateCertificationTrustPath_ValidChain() {
        // Prepare a valid chain
        val chain = mutableListOf<X509Certificate>()
        val certificate1 = loadCert()
        chain.add(certificate1)

        // Call the method under test
        val result = readerTrustStore.createCertificationTrustPath(chain)

        // Assert the result
        assertEquals(chain.size + 1, result?.size)
        assertEquals(certificate1, result?.get(0))
    }

    @Test
    fun testCreateCertificationTrustPath_InvalidChain() {
        // Prepare an invalid chain
        val chain = mutableListOf<X509Certificate>()
        val certificate1 = loadInvalidCert()
        chain.add(certificate1)

        // Call the method under test
        val result = readerTrustStore.createCertificationTrustPath(chain)

        // Assert the result
        assertEquals(null, result)
    }

    @Test
    fun testValidateCertificationTrustPath() {
        // Prepare an invalid chain
        val chain = mutableListOf<X509Certificate>()
        val certificate1 = loadCert()
        chain.add(certificate1)

        // Call the method under test
        val result = readerTrustStore.validateCertificationTrustPath(chain)

        // Assert the result
        assertTrue(result)
    }

    @Test
    @Ignore("This test depends on real certificates that may expire")
    fun testChain() {

        val pemToCertificate = { pem: String ->
            ByteArrayInputStream(Base64.getDecoder().decode(pem)).use {
                CertificateFactory.getInstance("X.509").generateCertificate(it)
            } as X509Certificate
        }

        val certificateChain = arrayOf(
            "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",
        ).map { pemToCertificate(it) }

        val profileValidation = spyk(ProfileValidation.DEFAULT)
        val trustStore = ReaderTrustStoreImpl(
            trustedCertificates = listOf(certificateChain.last()),
            profileValidation = profileValidation
        )
        val result = trustStore.validateCertificationTrustPath(certificateChain)
        assertTrue(result)

        verify(exactly = 1) {
            profileValidation.validate(any(), any())
        }
    }
}