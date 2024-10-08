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

import com.android.identity.crypto.Algorithm
import com.android.identity.document.DocumentRequest
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.KeyUnlockData
import eu.europa.ec.eudi.wallet.document.ElementIdentifier
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.NameSpace
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant

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
     * @param signatureAlgorithm the signature algorithm to use for the response
     * @throws IllegalArgumentException if the document format is not MsoMdocFormat, the document key is invalidated,
     * @throws com.android.identity.securearea.KeyLockedException if the document key is locked and cannot be unlocked
     */
    @JvmStatic
    @JvmOverloads
    fun generate(
        document: IssuedDocument,
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null,
        signatureAlgorithm: Algorithm = Algorithm.ES256
    ): ByteArray {
        require(document.format is MsoMdocFormat) { "Document format is not MsoMdocFormat" }
        require(!document.isKeyInvalidated) { "Document key is invalidated" }
        require(document.isValidAt(Clock.System.now().toJavaInstant())) { "Document is not valid" }
        val docType = (document.format as MsoMdocFormat).docType
        val dataElements =
            (elements ?: document.nameSpaces).flatMap { (nameSpace, elementIdentifiers) ->
                elementIdentifiers.map { elementIdentifier ->
                    DocumentRequest.DataElement(nameSpace, elementIdentifier, false)
                }
            }
        val request = DocumentRequest(dataElements)

        val staticAuthData = StaticAuthDataParser(document.issuerProvidedData).parse()
        val mergedIssuerNamespaces = MdocUtil.mergeIssuerNamesSpaces(
            request, document.nameSpacedData, staticAuthData
        )
        return DocumentGenerator(docType, staticAuthData.issuerAuth, transcript)
            .setIssuerNamespaces(mergedIssuerNamespaces)
            .setDeviceNamespacesSignature(
                dataElements = NameSpacedData.Builder().build(),
                secureArea = document.secureArea,
                keyAlias = document.keyAlias,
                keyUnlockData = keyUnlockData,
                signatureAlgorithm = signatureAlgorithm
            )
            .generate()
    }

    fun IssuedDocument.generateDocumentResponse(
        transcript: ByteArray,
        elements: Map<NameSpace, List<ElementIdentifier>>? = null,
        keyUnlockData: KeyUnlockData? = null,
        signatureAlgorithm: Algorithm = Algorithm.ES256
    ): Result<ByteArray> {
        return try {
            Result.success(
                generate(
                    this,
                    transcript,
                    elements,
                    keyUnlockData,
                    signatureAlgorithm
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}