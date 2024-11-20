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

package eu.europa.ec.eudi.iso18013.transfer.readerauth

/**
 * Interface that indicates that implementation are aware of a [ReaderTrustStore]
 * that can be used to verify the authenticity of a reader.
 *
 * @see [ReaderTrustStore]
 * @property readerTrustStore the reader trust store
 */
interface ReaderTrustStoreAware {
    var readerTrustStore: ReaderTrustStore?
}