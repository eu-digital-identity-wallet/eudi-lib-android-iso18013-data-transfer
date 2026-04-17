/*
 * Copyright (c) 2024-2026 European Commission
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
import kotlinx.coroutines.withContext
import org.multipaz.document.DocumentRequest
import org.multipaz.document.NameSpacedData
import org.multipaz.mdoc.credential.MdocCredential
import org.multipaz.mdoc.response.DocumentGenerator
import org.multipaz.mdoc.util.MdocUtil
import org.multipaz.prompt.Reason
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
        val provider = keyUnlockData.asProvider()
        return runBlocking {
            withContext(provider) {
                document.consumingCredential {
                    require(this is MdocCredential) { "Document must be in MsoMdocFormat" }
                    generateDocumentBytes(this, transcript, elements)
                }.getOrThrow()
            }
        }
    }

    /**
     * Generate a device response for a given document without consuming the credential.
     * Uses [IssuedDocument.findCredential] instead of [IssuedDocument.consumingCredential],
     * bypassing the [eu.europa.ec.eudi.wallet.document.CreateDocumentSettings.CredentialPolicy]
     * checks. This is used for ZK proof generation where the credential key is not sent
     * to the verifier and should not count against usage limits.
     *
     * @param document the document to generate the response for
     * @param transcript the transcript to use for the response
     * @param elements the elements to include in the response
     * @param keyUnlockData the key unlock data for unlocking the document key if needed
     * @throws IllegalArgumentException if the document format is not MsoMdocFormat
     * @throws IllegalStateException if no credential is found
     * @throws org.multipaz.securearea.KeyLockedException if the document key is locked and cannot be unlocked
     */
    @JvmStatic
    @JvmOverloads
    fun generateWithoutConsuming(
        document: IssuedDocument,
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null
    ): ByteArray {
        val provider = keyUnlockData.asProvider()
        return runBlocking {
            withContext(provider) {
                val credential = checkNotNull(document.findCredential()) {
                    "No credential found in the issued document"
                }
                require(credential is MdocCredential) { "Document must be in MsoMdocFormat" }
                generateDocumentBytes(credential, transcript, elements)
            }
        }
    }

    /**
     * Generates the signed device response bytes from an [MdocCredential].
     * Shared logic used by both [generate] and [generateWithoutConsuming].
     *
     * @param credential the mdoc credential to generate the response from
     * @param transcript the session transcript bytes
     * @param elements optional map of namespaces to element identifiers to include
     * @return the generated device response bytes
     */
    private suspend fun generateDocumentBytes(
        credential: MdocCredential,
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>?
    ): ByteArray {
        val credentialIssuedData =
            credential.getIssuedData<CredentialIssuedData.MsoMdoc>()
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

        return DocumentGenerator(credential.docType, staticAuthData.issuerAuth, transcript)
            .setIssuerNamespaces(mergedIssuerNamespaces)
            .setDeviceNamespacesSignature(
                dataElements = NameSpacedData.Builder().build(),
                secureArea = credential.secureArea,
                keyAlias = credential.alias,
                unlockReason = Reason.Unspecified
            )
            .generate()
    }

    /**
     * Extension function to generate a device response with credential consumption.
     * Wraps [generate] in a [Result] for safe error handling.
     *
     * @param transcript the transcript to use for the response
     * @param elements the elements to include in the response
     * @param keyUnlockData the key unlock data for unlocking the document key if needed
     * @return a [Result] containing the generated device response bytes or the error
     * @see generate
     */
    fun IssuedDocument.generateDocumentResponse(
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null
    ): Result<ByteArray> = runCatching { generate(this, transcript, elements, keyUnlockData) }

    /**
     * Extension function to generate a device response without consuming the credential.
     * Wraps [generateWithoutConsuming] in a [Result] for safe error handling.
     *
     * @param transcript the transcript to use for the response
     * @param elements the elements to include in the response
     * @param keyUnlockData the key unlock data for unlocking the document key if needed
     * @return a [Result] containing the generated device response bytes or the error
     * @see generateWithoutConsuming
     */
    fun IssuedDocument.generateDocumentResponseWithoutConsuming(
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null
    ): Result<ByteArray> = runCatching { generateWithoutConsuming(this, transcript, elements, keyUnlockData) }
}