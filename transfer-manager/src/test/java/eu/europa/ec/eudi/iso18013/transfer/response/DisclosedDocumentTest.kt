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

package eu.europa.ec.eudi.iso18013.transfer.response

import com.android.identity.securearea.KeyUnlockData
import eu.europa.ec.eudi.wallet.document.DocumentId
import io.mockk.every
import io.mockk.mockk
import kotlin.test.*

class DisclosedDocumentTest {

    private lateinit var documentId: DocumentId
    private lateinit var disclosedItems: List<DocItem>
    private lateinit var keyUnlockData: KeyUnlockData

    @BeforeTest
    fun setUp() {
        documentId = "testDocumentId"
        disclosedItems = listOf(DocItem("namespace", "element"))
        keyUnlockData = mockk()
    }

    @Test
    fun constructorShouldInitializePropertiesCorrectly() {
        val disclosedDocument = DisclosedDocument(documentId, disclosedItems, keyUnlockData)

        assertEquals(documentId, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun constructorShouldHandleNullKeyUnlockData() {
        val disclosedDocument = DisclosedDocument(documentId, disclosedItems, null)

        assertEquals(documentId, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertNull(disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldInitializePropertiesCorrectly() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.documentId } returns documentId
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(
                "namespace",
                "element"
            ) to true
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, disclosedItems, keyUnlockData)

        assertEquals(documentId, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldHandleNullDisclosedItems() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.documentId } returns documentId
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(
                "namespace",
                "element"
            ) to false
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, null, keyUnlockData)

        assertEquals(documentId, disclosedDocument.documentId)
        assertEquals(listOf(DocItem("namespace", "element")), disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldHandleNullKeyUnlockData() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.documentId } returns documentId
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(
                "namespace",
                "element"
            ) to false
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, disclosedItems, null)

        assertEquals(documentId, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertNull(disclosedDocument.keyUnlockData)
    }
}