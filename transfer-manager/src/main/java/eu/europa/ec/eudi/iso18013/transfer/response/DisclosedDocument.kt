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

/**
 * Represents a response that contains the document data that will be sent to an mdoc verifier
 *
 * @property documentId the unique id of the document stored in identity credential api
 * @property disclosedItems a [List] that contains the document items [DocItem], i.e the namespaces and the data elements that will be sent in the device response after selective disclosure
 * @property keyUnlockData the key unlock data that will be used to unlock document's key for signing the response
 */
data class DisclosedDocument(
    val documentId: DocumentId,
    val disclosedItems: List<DocItem>,
    val keyUnlockData: KeyUnlockData? = null,
) {
    /**
     * Alternative constructor that takes a [RequestedDocument] and a [List] of [DocItem] to create a [DisclosedDocument]
     * @param requestedDocument the requested document
     * @param disclosedItems the list of disclosed items. If not provided, it will be set to the list of requested items
     * @param keyUnlockData the key unlock data
     */
    constructor(
        requestedDocument: RequestedDocument,
        disclosedItems: List<DocItem>? = null,
        keyUnlockData: KeyUnlockData? = null
    ) : this(
        requestedDocument.documentId,
        disclosedItems ?: requestedDocument.requestedItems.keys.toList(),
        keyUnlockData
    )
}