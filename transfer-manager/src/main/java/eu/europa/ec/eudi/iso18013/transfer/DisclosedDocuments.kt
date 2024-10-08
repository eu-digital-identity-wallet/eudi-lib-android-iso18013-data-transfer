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
@file:JvmMultifileClass

package eu.europa.ec.eudi.iso18013.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a response that contains the document data that will be sent to an mdoc verifier
 */
@Parcelize
data class DisclosedDocuments(val documents: List<DisclosedDocument>) : Parcelable

/**
 * Represents a response that contains the document data that will be sent to an mdoc verifier
 *
 * @property documentId the unique id of the document stored in identity credential api
 * @property docType the document type, e.g. eu.europa.ec.eudiw.pid.1
 * @property selectedDocItems a [List] that contains the document items [DocItem], i.e the namespaces and the data elements that will be sent in the device response after selective disclosure
 * @property docRequest the received document request
 * @constructor Create empty Response document data
 */
@Parcelize
class DisclosedDocument(
    val documentId: String,
    val docType: String,
    val selectedDocItems: List<DocItem>,
    val docRequest: DocRequest,
) : Parcelable
