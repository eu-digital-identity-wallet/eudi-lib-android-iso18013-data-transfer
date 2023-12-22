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
package eu.europa.ec.eudi.iso18013.transfer.internal.readerauth

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.crl.CertificateCRLValidation
import eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.profile.ProfileValidation
import eu.europa.ec.eudi.iso18013.transfer.mockAndroidLog
import eu.europa.ec.eudi.iso18013.transfer.readerauth.loadCert
import eu.europa.ec.eudi.iso18013.transfer.readerauth.loadInvalidCert
import eu.europa.ec.eudi.iso18013.transfer.readerauth.loadTrustCert
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockedStatic
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import java.security.cert.X509Certificate

@RunWith(JUnit4::class)
class ReaderTrustStoreImplTest {

    private lateinit var readerTrustStore: ReaderTrustStoreImpl
    private lateinit var trustedCertificates: List<X509Certificate>

    private lateinit var mockLog: MockedStatic<Log>

    @Before
    fun setup() {
        trustedCertificates = listOf(loadTrustCert())
        readerTrustStore = ReaderTrustStoreImpl(trustedCertificates, ProfileValidation.DEFAULT)

        mockLog = mockAndroidLog()
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

        mockStatic(CertificateCRLValidation::class.java).apply {
            `when`(CertificateCRLValidation.verify(any())).thenAnswer { }
        }

        // Call the method under test
        val result = readerTrustStore.validateCertificationTrustPath(chain)

        // Assert the result
        assertTrue(result)
    }

    @After
    fun close() {
        mockLog.close()
    }
}
