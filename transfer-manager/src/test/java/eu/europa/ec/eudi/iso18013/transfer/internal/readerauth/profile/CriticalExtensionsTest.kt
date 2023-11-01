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
package eu.europa.ec.eudi.iso18013.transfer.internal.readerauth.profile

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.readerauth.loadCert
import eu.europa.ec.eudi.iso18013.transfer.readerauth.loadTrustCert
import org.bouncycastle.asn1.x509.Extension.policyMappings
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import java.security.cert.X509Certificate

@RunWith(JUnit4::class)
class CriticalExtensionsTest {

    private lateinit var readerAuthCertificate: X509Certificate
    private lateinit var trustCA: X509Certificate

    private lateinit var validation: ProfileValidation

    private lateinit var mockLog: MockedStatic<Log>

    @Before
    fun setup() {
        readerAuthCertificate = loadCert()
        trustCA = loadTrustCert()
        validation = CriticalExtensions()

        mockLog = Mockito.mockStatic(Log::class.java).apply {
            `when`(Log.d(Mockito.anyString(), Mockito.anyString(), any())).thenAnswer { 1 }
        }
    }

    @Test
    fun testVerify_Valid() {
        // Call the method under test
        val result = validation.validate(readerAuthCertificate, trustCA)

        // Assert the result
        assertTrue(result)
    }

    @Test
    fun testVerify_Invalid() {
        val mockCert = mock(X509Certificate::class.java)

        `when`(mockCert.criticalExtensionOIDs).thenReturn(setOf<String>(policyMappings.id))

        // Call the method under test
        val result = validation.validate(mockCert, trustCA)

        // Assert the result
        assertFalse(result)
    }

    @After
    fun close() {
        mockLog.close()
    }
}
