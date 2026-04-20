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

import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocument
import eu.europa.ec.eudi.iso18013.transfer.response.device.MsoMdocItem
import org.multipaz.mdoc.zkp.ZkSystem
import org.multipaz.mdoc.zkp.ZkSystemRepository
import org.multipaz.mdoc.zkp.ZkSystemSpec
import org.multipaz.request.MdocRequestedClaim

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
 * Match a ZKP system against the claims the user selected to disclose.
 *
 * Returns null when [zkSystemRepository] is not configured, the verifier did not request ZKP,
 * or no compatible system spec was found for the disclosed claim set.
 *
 * @param zkSystemRepository the ZKP system repository, or null if not configured
 * @param requestedDocument the original request carrying the verifier's ZK system specs and intentToRetain
 * @param disclosedDocument the document the user selected to disclose
 * @param docType the mdoc docType the claims belong to
 */
internal fun matchZkSystem(
    zkSystemRepository: ZkSystemRepository?,
    requestedDocument: RequestedDocument,
    disclosedDocument: DisclosedDocument,
    docType: String,
): MatchedZkSystem? = zkSystemRepository?.let { repo ->
    requestedDocument.zkRequestSystemSpecs?.let { specs ->
        val requestedClaims = disclosedDocument.disclosedItems
            .filterIsInstance<MsoMdocItem>()
            .map { item ->
                MdocRequestedClaim(
                    docType = docType,
                    namespaceName = item.namespace,
                    dataElementName = item.elementIdentifier,
                    intentToRetain = requestedDocument.requestedItems[item]!!,
                )
            }
        findMatchingZkSystem(
            zkSystemRepository = repo,
            zkSystemSpecs = specs,
            requestedClaims = requestedClaims,
        )
    }
}

/**
 * Find the ZKP system that matches the requested claims against the verifier's
 * requested [zkSystemSpecs].
 *
 * @param zkSystemRepository the ZKP proof system repository
 * @param zkSystemSpecs the ZKP system specs requested by the verifier
 * @param requestedClaims the requested claims the proof will cover
 * @return the matched ZKP system and its specification, or null if none found
 */
private fun findMatchingZkSystem(
    zkSystemRepository: ZkSystemRepository,
    zkSystemSpecs: List<ZkSystemSpec>,
    requestedClaims: List<MdocRequestedClaim>,
): MatchedZkSystem? {
    if (zkSystemSpecs.isEmpty()) return null

    return zkSystemSpecs
        .asSequence()
        .mapNotNull { zkSpec ->
            val system = zkSystemRepository.lookup(zkSpec.system) ?: return@mapNotNull null
            val spec = system.getMatchingSystemSpec(
                zkSystemSpecs = zkSystemSpecs,
                requestedClaims = requestedClaims
            ) ?: return@mapNotNull null
            MatchedZkSystem(system, spec)
        }.firstOrNull()
}