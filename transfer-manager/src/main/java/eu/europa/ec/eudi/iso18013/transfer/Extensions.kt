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
@file:JvmName("Extensions")

package eu.europa.ec.eudi.iso18013.transfer

import eu.europa.ec.eudi.iso18013.transfer.response.DocItem
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.NameSpace

/**
 * Converts a [List] of [DocItem] to a [Map] of [NameSpace] to [List] of [ElementIdentifier]
 * @receiver the [List] of [DocItem] to convert
 * @return the [Map] of [NameSpace] to [List] of [ElementIdentifier]
 */
@JvmName("docItemsToNameSpaces")
fun List<DocItem>.asMap(): Map<NameSpace, List<ElementIdentifier>> = this
    .groupBy { (nameSpace, _) -> nameSpace }
    .mapValues { (_, docItems) -> docItems.map { it.elementIdentifier } }

/**
 * Converts a [Map] of [NameSpace] to [List] of [ElementIdentifier] to a [List] of [DocItem]
 * @receiver the [Map] of [NameSpace] to [List] of [ElementIdentifier] to convert
 * @return the [List] of [DocItem]
 */
@JvmName("nameSpacesToDocItems")
fun Map<NameSpace, List<ElementIdentifier>>.toDocItems(): List<DocItem> =
    this.flatMap { (nameSpace, elementIdentifiers) ->
        elementIdentifiers.map { elementIdentifier ->
            DocItem(
                namespace = nameSpace,
                elementIdentifier = elementIdentifier
            )
        }
    }

/**
 * Converts a [RequestProcessor.ProcessedRequest] to a [Result] of [RequestProcessor.ProcessedRequest.Success]
 * @receiver the [RequestProcessor.ProcessedRequest] to convert
 * @return the [Result] of [RequestProcessor.ProcessedRequest.Success]
 */
fun RequestProcessor.ProcessedRequest.toKotlinResult(): Result<RequestProcessor.ProcessedRequest.Success> {
    return when (this) {
        is RequestProcessor.ProcessedRequest.Success -> Result.success(this)
        is RequestProcessor.ProcessedRequest.Failure -> Result.failure(this.error)
    }
}

/**
 * Converts a [ResponseResult] to a [Result] of [ResponseResult.Success]
 * @receiver the [ResponseResult] to convert
 * @return the [Result] of [ResponseResult.Success]
 */
fun ResponseResult.toKotlinResult(): Result<ResponseResult.Success> {
    return when (this) {
        is ResponseResult.Success -> Result.success(this)
        is ResponseResult.Failure -> Result.failure(this.throwable)
    }
}