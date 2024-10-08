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

package eu.europa.ec.eudi.iso18013.transfer

import android.content.Context
import com.android.identity.securearea.PassphraseConstraints
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import eu.europa.ec.eudi.iso18013.transfer.response.device.DeviceRequest
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.DocumentManagerImpl
import eu.europa.ec.eudi.wallet.document.getResourceAsText
import eu.europa.ec.eudi.wallet.document.sample.SampleDocumentManagerImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

val StorageEngine = EphemeralStorageEngine()
val SecureArea = SoftwareSecureArea(StorageEngine)
val Context: Context = mockk {
    every { applicationContext } returns this
    every { mainLooper } returns mockk(relaxed = true)
}
const val KeyLockPassphrase = "1234"

val DocumentManagerWithKeyLock: DocumentManager by lazy {
    createDocumentManager(KeyLockPassphrase)
}

val DocumentManagerWithoutKeyLock: DocumentManager by lazy {
    createDocumentManager(null)
}

@OptIn(ExperimentalEncodingApi::class)
fun createDocumentManager(keyLockPassphrase: String?): DocumentManager {
    val storageEngine = EphemeralStorageEngine()
    return SampleDocumentManagerImpl(
        DocumentManagerImpl(
            storageEngine = storageEngine,
            secureArea = SoftwareSecureArea(storageEngine)
        )
    ).apply {
        loadMdocSampleDocuments(
            sampleData = Base64.decode(getResourceAsText("sample_documents.txt")),
            createKeySettings = keyLockPassphrase?.let {
                SoftwareCreateKeySettings.Builder()
                    .setPassphraseRequired(
                        true,
                        keyLockPassphrase,
                        PassphraseConstraints.PIN_FOUR_DIGITS
                    )
                    .build()
            } ?: SoftwareCreateKeySettings.Builder().build(),
            documentNamesMap = mapOf(
                "eu.europa.ec.eudi.pid.1" to "EU PID",
                "org.iso.18013.5.1.mDL" to "mDL"
            )
        )
    }
}

/**
 * namespace = "org.iso.18013.5.1", elementIdentifier = "given_name", intentToRetail = true
 * namespace = "org.iso.18013.5.1", elementIdentifier = "birth_date", intentToRetail = true
 * namespace = "org.iso.18013.5.1", elementIdentifier = "issue_date", intentToRetail = true
 * namespace = "org.iso.18013.5.1", elementIdentifier = "portrait", intentToRetail = false
 * namespace = "org.iso.18013.5.1.Utopia", elementIdentifier = "UtopiaID", intentToRetail = true
 */
@OptIn(ExperimentalStdlibApi::class)
val DeviceRequest: DeviceRequest = DeviceRequest(
    sessionTranscriptBytes = byteArrayOf(0),
    deviceRequestBytes = getResourceAsText("device_request.hex").hexToByteArray()
)

