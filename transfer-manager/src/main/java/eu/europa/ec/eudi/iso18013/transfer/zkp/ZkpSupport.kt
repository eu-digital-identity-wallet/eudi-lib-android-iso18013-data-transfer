/*
 * Copyright (c) 2025 European Commission
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

package eu.europa.ec.eudi.iso18013.transfer.zkp

import org.multipaz.documenttype.DocumentTypeRepository
import org.multipaz.mdoc.request.DocRequest
import org.multipaz.mdoc.zkp.ZkSystem
import org.multipaz.mdoc.zkp.ZkSystemRepository
import org.multipaz.mdoc.zkp.ZkSystemSpec

/**
 * Data class representing a matched zero-knowledge proof system along with its specification.
 *
 * @property system the zero-knowledge proof system
 * @property spec the specification of the zero-knowledge proof system
 */
data class MatchedZkSystem(
    val system: ZkSystem,
    val spec: ZkSystemSpec
)

/**
 * Find the matched zero-knowledge proof system for the [DocRequest].
 * @param zkSystemRepository the zero-knowledge proof system repository
 * @param documentTypeRepository the document type repository
 * @return the matched zero-knowledge proof system and its specification, or null if none found
 */
internal fun DocRequest.findMatchedZkSystem(
    zkSystemRepository: ZkSystemRepository,
    documentTypeRepository: DocumentTypeRepository = DocumentTypeRepository(),
): MatchedZkSystem? {

    val zkRequestSystemSpecs = docRequestInfo?.zkRequest?.systemSpecs ?: return null
    if (zkRequestSystemSpecs.isEmpty()) return null

    val requestedClaims = toMdocRequest(
        documentTypeRepository = documentTypeRepository,
        mdocCredential = null
    ).requestedClaims

    return zkRequestSystemSpecs
        .asSequence()
        .mapNotNull { zkSpec ->
            val system = zkSystemRepository.lookup(zkSpec.system) ?: return@mapNotNull null
            val spec = system.getMatchingSystemSpec(
                zkSystemSpecs = zkRequestSystemSpecs,
                requestedClaims = requestedClaims
            ) ?: return@mapNotNull null
            MatchedZkSystem(system, spec)
        }.firstOrNull()
}