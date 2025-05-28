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

package eu.europa.ec.eudi.iso18013.transfer.response.device

import eu.europa.ec.eudi.iso18013.transfer.DeviceRequest
import eu.europa.ec.eudi.iso18013.transfer.DocumentManagerWithKeyLock
import eu.europa.ec.eudi.iso18013.transfer.DocumentManagerWithoutKeyLock
import eu.europa.ec.eudi.iso18013.transfer.KeyLockPassphrase
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor.ProcessedRequest.Failure
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.toDocItems
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import io.mockk.mockk
import org.junit.Test
import org.multipaz.crypto.Algorithm
import org.multipaz.securearea.KeyLockedException
import org.multipaz.securearea.software.SoftwareKeyUnlockData
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DeviceRequestProcessorTest {

    @Test
    fun `process should return a RequestedDocuments containing only the documents found matching docType for given DeviceRequest`() {
        val documentManager = DocumentManagerWithoutKeyLock
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
        val documentManager = DocumentManagerWithoutKeyLock

        val requestProcessor = DeviceRequestProcessor(documentManager)
        val request = mockk<Request>()
        val processedRequest = requestProcessor.process(request)
        assertIs<Failure>(processedRequest)
    }

    @Test
    fun `processed request should generate response`() {
        val documentManager = DocumentManagerWithoutKeyLock
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
        val documentManager = DocumentManagerWithKeyLock
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
    fun `processed request should return success if key needs unlock and keyUnlock provided`() {
        val documentManager = DocumentManagerWithKeyLock
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
}