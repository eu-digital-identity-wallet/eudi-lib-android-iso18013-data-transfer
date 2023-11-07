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
package eu.europa.ec.eudi.iso18013.transfer.internal

import com.android.identity.android.mdoc.transport.DataTransportOptions
import com.android.identity.mdoc.connectionmethod.ConnectionMethod
import com.android.identity.mdoc.connectionmethod.ConnectionMethodBle
import eu.europa.ec.eudi.iso18013.transfer.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.retrieval.BleRetrievalMethod
import java.util.UUID

internal val DeviceRetrievalMethod.connectionMethod: List<ConnectionMethod>
    get() = when (this) {
        is BleRetrievalMethod -> {
            mutableListOf<ConnectionMethod>().apply {
                val randomUUID = UUID.randomUUID()
                add(
                    ConnectionMethodBle(
                        peripheralServerMode,
                        centralClientMode,
                        if (peripheralServerMode) randomUUID else null,
                        if (centralClientMode) randomUUID else null
                    )
                )
            }
        }

        else -> throw IllegalArgumentException("Unsupported connection method")
    }

internal val List<DeviceRetrievalMethod>.transportOptions: DataTransportOptions
    get() = DataTransportOptions.Builder().apply {
        for (m in this@transportOptions) {
            if (m is BleRetrievalMethod) setBleClearCache(m.clearBleCache)
        }
    }.build()

internal val List<DeviceRetrievalMethod>.connectionMethods: List<ConnectionMethod>
    get() = flatMap { it.connectionMethod }
