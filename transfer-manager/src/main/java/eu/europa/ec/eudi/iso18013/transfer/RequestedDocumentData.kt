/*
 * Copyright (c) 2023 European Commission
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

@Parcelize
data class RequestedDocumentData(val documents: List<RequestDocument>) : Parcelable

/**
 * Represents a request received by an mdoc verifier and contains the requested document data
 *
 * @property documentId the unique id of the document stored in identity credential api
 * @property docType the document type, e.g. eu.europa.ec.eudiw.pid.1
 * @property docName is the name of document that is displayed to the user
 * @property userAuthentication whether user authentication is required to access the document
 * @property docRequest the document request [DocRequest]
 * @constructor Create empty Request document data
 */
@Parcelize
data class RequestDocument(
    val documentId: String,
    val docType: String,
    val docName: String,
    val userAuthentication: Boolean,
    val docRequest: DocRequest,
) : Parcelable
