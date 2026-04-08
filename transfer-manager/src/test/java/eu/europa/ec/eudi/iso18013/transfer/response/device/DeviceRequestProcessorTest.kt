/*
 * Copyright (c) 2024-2026 European Commission
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

package eu.europa.ec.eudi.iso18013.transfer.response.device

import android.util.Log
import eu.europa.ec.eudi.iso18013.transfer.DeviceRequest
import eu.europa.ec.eudi.iso18013.transfer.KeyLockPassphrase
import eu.europa.ec.eudi.iso18013.transfer.createDocumentManager
import eu.europa.ec.eudi.iso18013.transfer.mockAndroidLog
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuth
import eu.europa.ec.eudi.iso18013.transfer.response.ReaderAuthPolicy
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor.ProcessedRequest.Failure
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.toDocItems
import eu.europa.ec.eudi.iso18013.transfer.zkp.MatchedZkSystem
import eu.europa.ec.eudi.iso18013.transfer.zkp.ZkResponsePolicy
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.MockedStatic
import org.multipaz.crypto.Algorithm
import org.multipaz.mdoc.response.DeviceResponseParser
import org.multipaz.mdoc.zkp.ZkSystem
import org.multipaz.mdoc.zkp.ZkSystemSpec
import org.multipaz.securearea.KeyLockedException
import org.multipaz.securearea.software.SoftwareKeyUnlockData
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

class DeviceRequestProcessorTest {
    lateinit var mockLog: MockedStatic<Log>

    @BeforeTest
    fun setUp() {
        mockLog = mockAndroidLog()
    }

    @AfterTest
    fun tearDown() {
        mockLog.close()
    }

    @Test
    fun `process should return a RequestedDocuments containing only the documents found matching docType for given DeviceRequest`() {
        val documentManager = createDocumentManager(null)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }

        val requestProcessor = DeviceRequestProcessor(documentManager)
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        assertEquals(1, processedRequest.requestedDocuments.size)
        assertEquals(expectedDocument.id, processedRequest.requestedDocuments[0].documentId)
        assertEquals(
            setOf(
                Pair(MsoMdocItem("org.iso.18013.5.1", "given_name"), true),
                Pair(MsoMdocItem("org.iso.18013.5.1", "birth_date"), true),
                Pair(MsoMdocItem("org.iso.18013.5.1", "issue_date"), true),
                Pair(MsoMdocItem("org.iso.18013.5.1", "portrait"), false),
                Pair(MsoMdocItem("org.iso.18013.5.1.Utopia", "UtopiaID"), true),
            ),
            processedRequest.requestedDocuments[0].requestedItems.entries.map { it.toPair() }
                .toSet()
        )
    }

    @Test
    fun `process should return a Failure result when request is not DeviceRequest`() {
        val documentManager = createDocumentManager(null)

        val requestProcessor = DeviceRequestProcessor(documentManager)
        val request = mockk<Request>()
        val processedRequest = requestProcessor.process(request)
        assertIs<Failure>(processedRequest)
    }

    @Test
    fun `processed request should generate response`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedRequest.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        assertIs<DeviceResponse>(responseResult.response)

    }

    @Test
    fun `processed request should return failure if key needs unlock and no keyUnlock provided`() {
        val documentManager = createDocumentManager(KeyLockPassphrase)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedRequest.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Failure>(responseResult)
        assertIs<KeyLockedException>(responseResult.throwable)

    }

    @Test
    fun `generateResponse should include only the requested items even when more are disclosed`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        // Disclose ALL items from the document (a superset of what was requested).
        val allDisclosedItems = documentData.nameSpaces.toDocItems()
        val responseResult = processedRequest.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = allDisclosedItems,
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response
        assertIs<DeviceResponse>(deviceResponse)

        // Intersection of the requested items (from the DeviceRequest fixture)
        // and the items actually present in the sample document. The request
        // also asks for org.iso.18013.5.1.Utopia/UtopiaID, which the sample
        // document does not contain, so it is expected to be absent.
        val expectedItems = setOf(
            "org.iso.18013.5.1" to "given_name",
            "org.iso.18013.5.1" to "birth_date",
            "org.iso.18013.5.1" to "issue_date",
            "org.iso.18013.5.1" to "portrait",
        )

        val parsed = runBlocking {
            DeviceResponseParser(
                encodedDeviceResponse = deviceResponse.deviceResponseBytes,
                encodedSessionTranscript = deviceResponse.sessionTranscriptBytes,
            ).parse()
        }
        val parsedDocument = parsed.documents.single()
        val actualItems = parsedDocument.issuerNamespaces.flatMap { ns ->
            parsedDocument.getIssuerEntryNames(ns).map { ns to it }
        }.toSet()

        // Precondition: the document contains items beyond the requested set,
        // so the assertion below is a real filtering check and not vacuous.
        assertTrue(
            allDisclosedItems.size > expectedItems.size,
            "Test fixture precondition: the document must contain more items than were requested"
        )
        assertEquals(expectedItems, actualItems)
    }

    @Test
    fun `processed request should return success if key needs unlock and keyUnlock provided`() {
        val documentManager = createDocumentManager(KeyLockPassphrase)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedRequest.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                    keyUnlockData = SoftwareKeyUnlockData(KeyLockPassphrase)
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        assertIs<DeviceResponse>(responseResult.response)

    }

    @Test
    fun `EnforceIfPresent should skip documents with failed reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val failedReaderAuth = ReaderAuth(
            readerAuth = byteArrayOf(0),
            readerSignIsValid = false,
            readerCertificatedIsTrusted = false,
            readerCertificateChain = emptyList(),
            readerCommonName = "CN=Untrusted Reader",
        )

        val processedWithFailedAuth = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = failedReaderAuth)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.EnforceIfPresent,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithFailedAuth.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(emptyList(), deviceResponse.documentIds)
    }

    @Test
    fun `EnforceIfPresent should include documents with null reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val processedWithNullAuth = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = null)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.EnforceIfPresent,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithNullAuth.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(listOf(expectedDocument.id), deviceResponse.documentIds)
    }

    @Test
    fun `EnforceIfPresent should include documents with verified reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val verifiedReaderAuth = ReaderAuth(
            readerAuth = byteArrayOf(0),
            readerSignIsValid = true,
            readerCertificatedIsTrusted = true,
            readerCertificateChain = emptyList(),
            readerCommonName = "CN=Trusted Reader",
        )

        val processedWithVerifiedAuth = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = verifiedReaderAuth)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.EnforceIfPresent,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithVerifiedAuth.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(listOf(expectedDocument.id), deviceResponse.documentIds)
    }

    @Test
    fun `DoNotEnforce should include documents with failed reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val failedReaderAuth = ReaderAuth(
            readerAuth = byteArrayOf(0),
            readerSignIsValid = false,
            readerCertificatedIsTrusted = false,
            readerCertificateChain = emptyList(),
            readerCommonName = "CN=Untrusted Reader",
        )

        val processedWithDoNotEnforce = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = failedReaderAuth)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.DoNotEnforce,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithDoNotEnforce.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(listOf(expectedDocument.id), deviceResponse.documentIds)
    }

    @Test
    fun `AlwaysRequire should skip documents with failed reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val failedReaderAuth = ReaderAuth(
            readerAuth = byteArrayOf(0),
            readerSignIsValid = false,
            readerCertificatedIsTrusted = false,
            readerCertificateChain = emptyList(),
            readerCommonName = "CN=Untrusted Reader",
        )

        val processedWithAlwaysRequire = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = failedReaderAuth)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.AlwaysRequire,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithAlwaysRequire.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(emptyList(), deviceResponse.documentIds)
    }

    @Test
    fun `AlwaysRequire should include documents with verified reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val verifiedReaderAuth = ReaderAuth(
            readerAuth = byteArrayOf(0),
            readerSignIsValid = true,
            readerCertificatedIsTrusted = true,
            readerCertificateChain = emptyList(),
            readerCommonName = "CN=Trusted Reader",
        )

        val processedWithAlwaysRequire = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = verifiedReaderAuth)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.AlwaysRequire,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithAlwaysRequire.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(listOf(expectedDocument.id), deviceResponse.documentIds)
    }

    @Test
    fun `AlwaysRequire should skip documents with null reader authentication`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)

        val processedWithAlwaysRequire = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = byteArrayOf(0),
            requestedDocuments = RequestedDocuments(
                processedRequest.requestedDocuments.map { doc ->
                    doc.copy(readerAuth = null)
                }
            ),
            readerAuthPolicy = ReaderAuthPolicy.AlwaysRequire,
        )

        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val responseResult = processedWithAlwaysRequire.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        assertIs<ResponseResult.Success>(responseResult)
        val deviceResponse = responseResult.response as DeviceResponse
        assertEquals(emptyList(), deviceResponse.documentIds)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `processed request should return failure when ZK proof generation fails`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        // Create a matched ZK system that throws on generateProof
        val failingZkSystem = mockk<ZkSystem> {
            every { generateProof(any(), any(), any()) } throws RuntimeException("ZK proof generation failed")
        }
        val zkSystemSpec = mockk<ZkSystemSpec>()
        val matchedZkSystem = MatchedZkSystem(failingZkSystem, zkSystemSpec)

        // Rebuild requestedDocuments with the failing matchedZkSystem
        val requestedDocumentsWithZk = RequestedDocuments(
            processedRequest.requestedDocuments.map { reqDoc ->
                RequestedDocument(
                    documentId = reqDoc.documentId,
                    requestedItems = reqDoc.requestedItems,
                    readerAuth = reqDoc.readerAuth,
                    matchedZkSystem = matchedZkSystem
                )
            }
        )

        val processedRequestWithZk = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = DeviceRequest.sessionTranscriptBytes,
            requestedDocuments = requestedDocumentsWithZk,
            zkResponsePolicy = ZkResponsePolicy.Strict,
        )

        val responseResult = processedRequestWithZk.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        // ZK proof failure must NOT silently fall back to full disclosure
        assertIs<ResponseResult.Failure>(responseResult)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `processed request should return success with fallback policy when ZK proof generation fails`() {
        val documentManager = createDocumentManager(null)
        val requestProcessor = DeviceRequestProcessor(documentManager)
        val expectedDocument = documentManager.getDocuments()
            .filterIsInstance<IssuedDocument>()
            .first { it.format is MsoMdocFormat && (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL" }
        val processedRequest = requestProcessor.process(DeviceRequest)
        assertIs<ProcessedDeviceRequest>(processedRequest)
        val documentData = expectedDocument.data
        assertIs<MsoMdocData>(documentData)

        val failingZkSystem = mockk<ZkSystem> {
            every { generateProof(any(), any(), any()) } throws RuntimeException("ZK proof generation failed")
        }
        val zkSystemSpec = mockk<ZkSystemSpec>()
        val matchedZkSystem = MatchedZkSystem(failingZkSystem, zkSystemSpec)

        val requestedDocumentsWithZk = RequestedDocuments(
            processedRequest.requestedDocuments.map { reqDoc ->
                RequestedDocument(
                    documentId = reqDoc.documentId,
                    requestedItems = reqDoc.requestedItems,
                    readerAuth = reqDoc.readerAuth,
                    matchedZkSystem = matchedZkSystem
                )
            }
        )

        val processedRequestWithZk = ProcessedDeviceRequest(
            documentManager = documentManager,
            sessionTranscript = DeviceRequest.sessionTranscriptBytes,
            requestedDocuments = requestedDocumentsWithZk,
            zkResponsePolicy = ZkResponsePolicy.FallbackToFullDisclosure,
        )

        val responseResult = processedRequestWithZk.generateResponse(
            disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    documentId = expectedDocument.id,
                    disclosedItems = documentData.nameSpaces.toDocItems(),
                )
            ),
            signatureAlgorithm = Algorithm.ES256,
        )

        // FallbackToFullDisclosure should return success even when ZK proof fails
        assertIs<ResponseResult.Success>(responseResult)
        assertIs<DeviceResponse>(responseResult.response)
    }
}