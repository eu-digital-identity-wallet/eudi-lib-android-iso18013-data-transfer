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

import eu.europa.ec.eudi.iso18013.transfer.response.DocItem
import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.NameSpace

@JvmName("docItemsToNameSpaces")
fun List<DocItem>.asMap(): Map<NameSpace, List<ElementIdentifier>> = this
    .groupBy { (nameSpace, _) -> nameSpace }
    .mapValues { (_, docItems) -> docItems.map { it.elementIdentifier } }

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