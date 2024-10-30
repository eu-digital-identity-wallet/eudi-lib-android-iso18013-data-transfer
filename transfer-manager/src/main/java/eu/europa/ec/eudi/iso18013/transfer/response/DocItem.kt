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

/**
 * Doc item represents a data element
 *
 * @property namespace the namespace that the element belong to, e.g. eu.europa.ec.eudiw.pid.1
 * @property elementIdentifier the data element identifier e.g. family_name, given_name
 */
data class DocItem(
    val namespace: String,
    val elementIdentifier: String
)