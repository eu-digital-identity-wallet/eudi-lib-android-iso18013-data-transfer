/*
 * Copyright (c) 2023-2024 European Commission
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

import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocument

/**
 * Represents the issuer signed items that will be sent to an mdoc verifier
 *
 * @return a [Map] containing the selected (e.g. after selective disclosure) data elements grouped by namespace
 */
internal val DisclosedDocument.issuerSignedItems: Map<String, Collection<String>>
    get() = selectedDocItems.groupBy { it.namespace }
        .map { (namespace, docItems) ->
            namespace to docItems.map { it.elementIdentifier }
        }
        .toMap()
