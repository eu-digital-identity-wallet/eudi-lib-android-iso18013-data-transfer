/*
 * Copyright (c) 2026 European Commission
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

/**
 * Policy that determines behavior when ZK proof generation fails during response generation.
 */
sealed interface ZkResponsePolicy {

    /**
     * Abort disclosure for the document if ZK proof generation fails.
     * Recommended for production use to prevent unintended full document disclosure.
     */
    data object Strict : ZkResponsePolicy

    /**
     * Fall back to full document disclosure if ZK proof generation fails.
     * This is the current default for backwards compatibility and will be changed
     * to [Strict] in a future release.
     */
    data object FallbackToFullDisclosure : ZkResponsePolicy
}
