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

package eu.europa.ec.eudi.iso18013.transfer.internal

import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.DocItem
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AgeOverRequestLimitTest {

    lateinit var issuedDocument: IssuedDocument

    @BeforeTest
    fun setup() {
        issuedDocument = mockk {
            every { id } returns "document_id"
            every { format } returns MsoMdocFormat(docType = "org.iso.18013.5.1.mDL")
        }
    }

    @Test
    fun `should return the issued document if the request contains 1 age_over_nn item`() {
        val disclosedDocument = DisclosedDocument(
            documentId = "document_id",
            disclosedItems = listOf(
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_18")
            )
        )
        val document = issuedDocument.assertAgeOverRequestLimitForIso18013(disclosedDocument)
        assertSame(issuedDocument, document)
    }

    @Test
    fun `should return the issued document if the request contains 2 age_over_nn item2`() {
        val disclosedDocument = DisclosedDocument(
            documentId = "document_id",
            disclosedItems = listOf(
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_18"),
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_21")
            )
        )
        val document = issuedDocument.assertAgeOverRequestLimitForIso18013(disclosedDocument)
        assertSame(issuedDocument, document)
    }

    @Test
    fun `should throw the issued document if the request contains more than 2 age_over_nn items`() {
        val disclosedDocument = DisclosedDocument(
            documentId = "document_id",
            disclosedItems = listOf(
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_18"),
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_21"),
                DocItem(namespace = "org.iso.18013.5.1", elementIdentifier = "age_over_65")
            )
        )
        val throwable = assertThrows(IllegalArgumentException::class.java) {
            issuedDocument.assertAgeOverRequestLimitForIso18013(disclosedDocument)
        }

        assertEquals(
            "Device Response is not allowed to have more than two age_over_NN elements",
            throwable.message
        )
    }
}