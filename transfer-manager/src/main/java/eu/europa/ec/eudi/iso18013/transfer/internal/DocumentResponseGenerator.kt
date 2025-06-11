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

import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.NameSpace
import eu.europa.ec.eudi.wallet.document.credential.CredentialIssuedData
import eu.europa.ec.eudi.wallet.document.credential.getIssuedData
import kotlinx.coroutines.runBlocking
import org.multipaz.document.DocumentRequest
import org.multipaz.document.NameSpacedData
import org.multipaz.mdoc.credential.MdocCredential
import org.multipaz.mdoc.response.DocumentGenerator
import org.multipaz.mdoc.util.MdocUtil
import org.multipaz.securearea.KeyUnlockData

internal object DocumentResponseGenerator {

    /**
     * Generate a device response for a given document.
     *
     * Document must be in MsoMdocFormat and not have an invalidated key.
     *
     * @param document the document to generate the response for
     * @param transcript the transcript to use for the response
     * @param elements the elements to include in the response
     * @param keyUnlockData the key unlock data for unlocking the document key if needed
     * @throws IllegalArgumentException if the document format is not MsoMdocFormat, the document key is invalidated,
     * @throws org.multipaz.securearea.KeyLockedException if the document key is locked and cannot be unlocked
     */
    @JvmStatic
    @JvmOverloads
    fun generate(
        document: IssuedDocument,
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null
    ): ByteArray {
        return runBlocking {
            document.consumingCredential {
                require(this is MdocCredential) { "Document must be in MsoMdocFormat" }
                val credentialIssuedData =
                    getIssuedData<CredentialIssuedData.MsoMdoc>()
                val (nameSpacedData, staticAuthData) = credentialIssuedData.getOrThrow()
                val dataElements = (elements ?: nameSpacedData.nameSpaceNames.associateWith {
                    nameSpacedData.getDataElementNames(it)
                }).flatMap { (nameSpace, elementIdentifiers) ->
                    elementIdentifiers.map { elementIdentifier ->
                        DocumentRequest.DataElement(nameSpace, elementIdentifier, false)
                    }
                }
                val request = DocumentRequest(dataElements)

                val mergedIssuerNamespaces = MdocUtil.mergeIssuerNamesSpaces(
                    request, nameSpacedData, staticAuthData
                )
                DocumentGenerator(docType, staticAuthData.issuerAuth, transcript)
                    .setIssuerNamespaces(mergedIssuerNamespaces)
                    .setDeviceNamespacesSignature(
                        dataElements = NameSpacedData.Builder().build(),
                        secureArea = secureArea,
                        keyAlias = alias,
                        keyUnlockData = keyUnlockData
                    )
                    .generate()
            }.getOrThrow()
        }
    }

    fun IssuedDocument.generateDocumentResponse(
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null
    ): Result<ByteArray> {
        return try {
            Result.success(
                generate(
                    this,
                    transcript,
                    elements,
                    keyUnlockData
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}