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
package eu.europa.ec.eudi.iso18013.transfer

import eu.europa.ec.eudi.iso18013.transfer.engagement.QrCode
import java.net.URI

sealed interface TransferEvent {

    data class QrEngagementReady(val qrCode: QrCode) : TransferEvent
    object Connecting : TransferEvent {
        override fun toString() = "Connecting"
    }

    object Connected : TransferEvent {
        override fun toString() = "Connected"
    }

    data class RequestReceived(val request: Request) : TransferEvent

    object ResponseSent : TransferEvent {
        override fun toString() = "ResponseSent"
    }

    data class Redirect(val redirectUri: URI) : TransferEvent

    object Disconnected : TransferEvent {
        override fun toString() = "Disconnected"
    }

    data class Error(val error: Throwable) : TransferEvent

    fun interface Listener {
        fun onTransferEvent(event: TransferEvent)
    }

    interface Listenable {
        /**
         * Add transfer event listener
         *
         * @param listener
         * @return a [Listenable]
         */
        fun addTransferEventListener(listener: Listener): Listenable

        /**
         * Remove transfer event listener
         *
         * @param listener
         * @return a [Listenable]
         */
        fun removeTransferEventListener(listener: Listener): Listenable

        /**
         * Remove all transfer event listeners
         *
         * @return a [Listenable]
         */
        fun removeAllTransferEventListeners(): Listenable
    }
}
