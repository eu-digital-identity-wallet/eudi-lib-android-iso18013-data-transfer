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
import eu.europa.ec.eudi.iso18013.transfer.response.device.MsoMdocItem
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat

/**
 * Asserts that the age over request limit for iso18013.5 is not exceeded
 * @throws Exception if the age over request limit is exceeded
 */
internal fun IssuedDocument.assertAgeOverRequestLimitForIso18013(disclosedDocument: DisclosedDocument): IssuedDocument =
    apply {
        val docType = (format as MsoMdocFormat).docType
        if (id == disclosedDocument.documentId && docType == "org.iso.18013.5.1.mDL" && disclosedDocument.disclosedItems
                .filterIsInstance<MsoMdocItem>()
                .filter { docItem ->
                    docItem.elementIdentifier.startsWith("age_over_") && docItem.namespace == "org.iso.18013.5.1"
                }.size > 2
        ) {
            throw IllegalArgumentException("Device Response is not allowed to have more than two age_over_NN elements")

        }
    }