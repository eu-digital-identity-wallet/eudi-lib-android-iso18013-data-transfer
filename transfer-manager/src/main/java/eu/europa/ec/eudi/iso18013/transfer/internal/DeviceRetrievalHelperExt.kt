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
package eu.europa.ec.eudi.iso18013.transfer.internal

import android.util.Log
import com.android.identity.android.mdoc.deviceretrieval.DeviceRetrievalHelper
import com.android.identity.util.Constants

internal fun DeviceRetrievalHelper.stopPresentation(
    sendSessionTerminationMessage: Boolean,
    useTransportSpecificSessionTermination: Boolean,
) {
    try {
        if (sendSessionTerminationMessage) {
            if (useTransportSpecificSessionTermination) {
                sendTransportSpecificTermination()
            } else {
                sendDeviceResponse(
                    null,
                    Constants.SESSION_DATA_STATUS_SESSION_TERMINATION,
                )
            }
        }
        disconnect()
    } catch (e: Exception) {
        Log.e(this.TAG, "Error ignored.", e)
    }
}
