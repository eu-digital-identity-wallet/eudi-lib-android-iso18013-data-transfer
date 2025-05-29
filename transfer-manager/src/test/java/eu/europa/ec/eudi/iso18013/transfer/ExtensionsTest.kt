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

package eu.europa.ec.eudi.iso18013.transfer


import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.response.device.MsoMdocItem
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExtensionsTest {

    @Test
    fun docItemsAsMapShouldGroupByNamespace() {
        val docItems = listOf(
            MsoMdocItem(namespace = "namespace1", elementIdentifier = "element1"),
            MsoMdocItem(namespace = "namespace1", elementIdentifier = "element2"),
            MsoMdocItem(namespace = "namespace2", elementIdentifier = "element3")
        )

        val result = docItems.asMap()

        assertEquals(2, result.size)
        assertEquals(2, result["namespace1"]?.size)
        assertEquals(1, result["namespace2"]?.size)
    }

    @Test
    fun nameSpacesToDocItemsShouldConvertMapToList() {
        val map = mapOf(
            "namespace1" to listOf("element1", "element2"),
            "namespace2" to listOf("element3")
        )

        val result = map.toDocItems()

        assertEquals(3, result.size)
        assertTrue(
            result.contains(
                MsoMdocItem(
                    namespace = "namespace1",
                    elementIdentifier = "element1"
                )
            )
        )
        assertTrue(
            result.contains(
                MsoMdocItem(
                    namespace = "namespace1",
                    elementIdentifier = "element2"
                )
            )
        )
        assertTrue(
            result.contains(
                MsoMdocItem(
                    namespace = "namespace2",
                    elementIdentifier = "element3"
                )
            )
        )
    }

    @Test
    fun processedRequestToKotlinResultShouldReturnSuccess() {
        val successRequest = mockk<RequestProcessor.ProcessedRequest.Success>()

        val result = successRequest.toKotlinResult()

        assertTrue(result.isSuccess)
    }

    @Test
    fun processedRequestToKotlinResultShouldReturnFailure() {
        val failureRequest = RequestProcessor.ProcessedRequest.Failure(Exception("Test Exception"))

        val result = failureRequest.toKotlinResult()

        assertTrue(result.isFailure)
    }

    @Test
    fun responseResultToKotlinResultShouldReturnSuccess() {
        val successResponse = ResponseResult.Success(mockk())

        val result = successResponse.toKotlinResult()

        assertTrue(result.isSuccess)
    }

    @Test
    fun responseResultToKotlinResultShouldReturnFailure() {
        val failureResponse = ResponseResult.Failure(Exception("Test Exception"))

        val result = failureResponse.toKotlinResult()

        assertTrue(result.isFailure)
    }
}