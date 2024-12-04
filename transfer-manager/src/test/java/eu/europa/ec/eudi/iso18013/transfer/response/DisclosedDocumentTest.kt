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
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import io.mockk.every
import io.mockk.mockk
import kotlin.test.*

class DisclosedDocumentTest {

    private lateinit var document: IssuedDocument
    private lateinit var disclosedItems: List<DocItem>
    private lateinit var keyUnlockData: KeyUnlockData

    @BeforeTest
    fun setUp() {
        document = mockk {
            every { id } returns "testDocumentId"
        }
        disclosedItems = listOf(DocItem(elementIdentifier = "element"))
        keyUnlockData = mockk()
    }

    @Test
    fun constructorShouldInitializePropertiesCorrectly() {
        val disclosedDocument = DisclosedDocument(document, disclosedItems, keyUnlockData)

        assertEquals(document, disclosedDocument.document)
        assertEquals(document.id, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun constructorShouldHandleNullKeyUnlockData() {
        val disclosedDocument = DisclosedDocument(document, disclosedItems, null)

        assertEquals(document, disclosedDocument.document)
        assertEquals(document.id, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertNull(disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldInitializePropertiesCorrectly() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.document } returns document
        every { requestedDocument.documentId } answers { callOriginal() }
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(elementIdentifier = "element") to true
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, disclosedItems, keyUnlockData)

        assertEquals(document, disclosedDocument.document)
        assertEquals(document.id, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldHandleNullDisclosedItems() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.document } returns document
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(elementIdentifier = "element") to false
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, null, keyUnlockData)

        assertEquals(document, disclosedDocument.document)
        assertEquals(document.id, disclosedDocument.documentId)
        assertEquals(listOf(DocItem("element")), disclosedDocument.disclosedItems)
        assertEquals(keyUnlockData, disclosedDocument.keyUnlockData)
    }

    @Test
    fun alternativeConstructorShouldHandleNullKeyUnlockData() {
        val requestedDocument = mockk<RequestedDocument>()
        every { requestedDocument.document } returns document
        every { requestedDocument.requestedItems } returns mapOf(
            DocItem(elementIdentifier = "element") to false
        )

        val disclosedDocument = DisclosedDocument(requestedDocument, disclosedItems, null)

        assertEquals(document, disclosedDocument.document)
        assertEquals(document.id, disclosedDocument.documentId)
        assertEquals(disclosedItems, disclosedDocument.disclosedItems)
        assertNull(disclosedDocument.keyUnlockData)
    }
}