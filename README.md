# EUDI ISO 18013-5 Wallet Transfer library for Android

:heavy_exclamation_mark: **Important!** Before you proceed, please read
the [EUDI Wallet Reference Implementation project description](https://github.com/eu-digital-identity-wallet/.github/blob/main/profile/reference-implementation.md)

## Overview

This library provides a set of classes to manage the transfer of documents in an EUDI ISO 18013-5
Android Wallet.

It defines the interfaces for TransferManager and Transfer classes and provides a standard
implementation of the TransferManager.

Currently, the library supports the following devices engagement methods:

- [x] QR
- [x] NFC tag
- [x] Reverse engagement with app link

and the transfer of the documents is done using Bluetooth Low Energy (BLE).

Additionally, Transfer Manager incorporates reader authentication, ensuring the trustworthiness of
the verifier (reader) device.

Reader authentication is accomplished by verifying the following:

- Certificate **path validation**: Verifying the certificate path from the device's certificate to a
  trusted root certificate
- Certificate's **profile validation**: Examines the attributes and constraints defined in the
  certificate profile to ensure that they meet the predefined criteria for a trusted certificate
- **CRL validation**: Checking the Certificate Revocation List to verify that the certificate has
  not been revoked or compromised.

The library is written in Kotlin and is available for Android.

## :heavy_exclamation_mark: Disclaimer

The released software is an initial development release version:

- The initial development release is an early endeavor reflecting the efforts of a short timeboxed
  period, and by no
  means can be considered as the final product.
- The initial development release may be changed substantially over time, might introduce new
  features but also may
  change or remove existing ones, potentially breaking compatibility with your existing code.
- The initial development release is limited in functional scope.
- The initial development release may contain errors or design flaws and other problems that could
  cause system or other
  failures and data loss.
- The initial development release has reduced security, privacy, availability, and reliability
  standards relative to
  future releases. This could make the software slower, less reliable, or more vulnerable to attacks
  than mature
  software.
- The initial development release is not yet comprehensively documented.
- Users of the software must perform sufficient engineering and additional testing in order to
  properly evaluate their
  application and determine whether any of the open-sourced components is suitable for use in that
  application.
- We strongly recommend not putting this version of the software into production use.
- Only the latest version of the software will be supported

## Requirements

- Android 8 (API level 26) or higher

### Dependencies

To use snapshot versions, add the following to your project's settings.gradle file:

```groovy
dependencyResolutionManagement {
    repositories {
        // ...
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            mavenContent { snapshotsOnly() }
        }
        // ...
    }
}
```

To include the library in your project, add the following dependencies to your app's build.gradle
file.

```groovy
dependencies {
    implementation "eu.europa.ec.eudi:eudi-lib-android-iso18013-data-transfer:0.4.0-SNAPSHOT"
}
```

## How to Use

Below is a quick overview of how to use the library.

For source code documentation, see in the [docs](docs/index.md) directory.

### Instantiating the TransferManager

The library provides a `TransferManager` implementation that can be used to present documents
using the ISO 18013-5 for proximity presentation and the ISO 18013-7 for remote presentation.

To create a new instance of the `TransferManager`, you can use the `TransferManager.getDefault`
method.

The following example demonstrates how to create a new instance of the `TransferManager`

```kotlin
import android.content.Context
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.TransferManagerImpl
import eu.europa.ec.eudi.iso18013.transfer.engagement.BleRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.wallet.document.DocumentManager

val documentManager: DocumentManager =
    TODO("The document manager to retrieve the requested documents")

val readerTrustStore = ReaderTrustStore.getDefault(
    trustedCertificates = listOf(
        // trustedReaderCertificate1,
        // trustedReaderCertificate2
    )
)

val transferManager = TransferManager.getDefault(
    context = context,
    documentManager = documentManager,
    retrievalMethods = listOf(
        BleRetrievalMethod(
            peripheralServerMode = true,
            centralClientMode = false,
            clearBleCache = true,
        )
    ),
    readerTrustStore = readerTrustStore,
)
``` 

### Attaching a TransferEvent.Listener

The transfer process is event-driven. To receive events, you need to attach a
`TransferEvent.Listener` to the `TransferManager`.

The available events are:

1. `TransferEvent.QrEngagementReady`:
   The QR code is ready to be displayed. Get the QR code from
   `TransferEvent.QrEngagementReady.qrCode`.
2. `TransferEvent.Connecting`: The devices are connecting. Use this event to display a progress
   indicator.
3. `TransferEvent.Connected`: the devices are connected.
4. `TransferEvent.RequestReceived`: A request is received. Get the processed request with the
   `TransferEvent.RequestReceived.processedRequest` and the initial raw request as received
   `TransferEvent.RequestReceived.request`.
5. `TransferEvent.ResponseSent`: A response is sent.
6. `TransferEvent.Redirect`: This event prompts to redirect the user to the given Redirect URI. Get
   the Redirect URI with `TransferEvent.Redirect.redirectUri`.
7. `TransferEvent.Disconnected`: The devices are disconnected.
8. `TransferEvent.Error`: An error occurred. Get the `Throwable` error with
   `TransferEvent.Error.error`.

The following example demonstrates how to attach a `TransferEvent.Listener` to the
`TransferManager`.
It also demonstrates how to handle the different events.

```kotlin
transferManager.addTransferEventListener { event ->
    when (event) {
        is TransferEvent.QrEngagementReady -> {
            // Qr code is ready to be displayed
            val qrCodeBitmap = event.qrCode.asBitmap(size = 800)
            // or
            val qrCodeView = event.qrCode.asView(context, size = 800)
        }

        TransferEvent.Connecting -> {
            // Informational event that devices are connecting
        }

        TransferEvent.Connected -> {
            // Informational event that the transfer has been connected
        }

        is TransferEvent.RequestReceived -> try {
            // assuming DeviceRequest is the request type
            val processedRequest = event.processedRequest.getOrThrow() as ProcessedDeviceRequest
            // the request has been received and processed

            // the request processing was successful
            // requested documents can be shown in the application
            val requestedDocuments = processedRequest.requestedDocuments
            // ...
            // application must create the DisclosedDocuments object
            val disclosedDocuments = DisclosedDocuments(
                // DisclosedDocument(),
                // DisclosedDocument(),
            )
            // generate the response
            val response = processedRequest.generateResponse(
                disclosedDocuments = disclosedDocuments,
                signatureAlgorithm = Algorithm.ES256
            ).getOrThrow() as DeviceResponse

            transferManager.sendResponse(response)

        } catch (e: Throwable) {
            // An error occurred
            // handle the error
        }

        TransferEvent.ResponseSent -> {
            // Informational event that the response has been sent
        }
        is TransferEvent.Redirect -> {
            // A redirect is needed. Used mainly for the OpenId4VP implementation
            val redirectUri = event.redirectUri // the redirect URI
        }
        TransferEvent.Disconnected -> {
            // Informational event that device has been disconnected
        }
        is TransferEvent.Error -> {
            // An error occurred
            val cause = event.error
        }
    }
}
```

### Initiating transfer

Transfer Manager provides the following methods for enabling device engagement and initiating the
transfer:

1. Scanning QR code
2. Reverse engagement using an app link
3. Using NFC .

These engagement methods offer seamless device pairing and data transfer initiation.

#### Using QR Code

With the `TransferManager` instance created, you can initiate the transfer with QR code by calling
the `TransferManager.startQrEngagement()` method. The method initiates the transfer process and
triggers the `TransferEvent.QrEngagementReady` event when the QR code is ready to be displayed.

#### Using App Link

To enable ISO 18013-7 REST API functionality, declare to your app's manifest file (
AndroidManifest.xml) an Intent Filter for your MainActivity:

```xml

<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="mdoc" android:host="*" />
</intent-filter>
```

and set `launchMode="singleTask"` for this activity.

To initiate the transfer using an app link (reverse engagement), use the
`TransferManager.startEngagementToApp(Intent)` method. The intent contains the mdoc uri for the
device engagement.

The example below demonstrates how to initiate the device engagement and transfer.

```kotlin
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // ... rest of activity code

    override fun onResume() {
        super.onResume()
        transferManager.startEngagementToApp(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        transferManager.startEngagementToApp(intent)
    }
}

```

#### Using NFC

To use NFC, you must create a service that extends the abstract `NfcEngagementService` and override
the `NfcEngagementService.transferManager` property.

For example:

```kotlin
package com.example.myapp

import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService

class NfcEngagementServiceImpl : NfcEngagementService() {
    override val transferManager: TransferManager
        get() = TODO("Return the transfer manager here")
}
```

Then add the service to your application's manifest file, like shown below:

```xml

<application>
    <!-- rest of manifest -->
    <service android:exported="true" android:label="@string/nfc_engagement_service_desc"
        android:name="com.example.myapp.NfcEngagementServiceImpl"
        android:permission="android.permission.BIND_NFC_SERVICE">
        <intent-filter>
            <action android:name="android.nfc.action.NDEF_DISCOVERED" />
            <action android:name="android.nfc.cardemulation.action.HOST_APDU_SERVICE" />
        </intent-filter>

        <!-- the following "@xml/nfc_engagement_apdu_service" in meta-data is provided by the library -->
        <meta-data android:name="android.nfc.cardemulation.host_apdu_service"
            android:resource="@xml/nfc_engagement_apdu_service" />
    </service>

</application>
```

You can enable or disable the NFC device engagement in your app by calling the
`NfcEngagementService.enable()` and `NfcEngagementService.disable()` methods.

In the example below, the NFC device engagement is enabled when activity is resumed and disabled
when the activity is paused.

```kotlin
import androidx.appcompat.app.AppCompatActivity
import eu.europa.ec.eudi.iso18013.transfer.engagement.NfcEngagementService

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        NfcEngagementService.enable(this)
    }

    override fun onPause() {
        super.onPause()
        NfcEngagementService.disable(this)
    }
}
```

Optionally, in the `NfcEngagementService.enable()` method you can define your class that implements
the abstract `NfcEngagementService`.

For example:

```kotlin
 NfcEngagementService.enable(this, NfcEngagementServiceImpl::class.java)
```

This way, you can define the `NfcEngagementServiceImpl` service to be preferred while this activity
is in the foreground.

### Receiving a request and sending a response

When a request is received, the `TransferManager` triggers the `TransferEvent.RequestReceived`.
The event contains the processed request and the initial raw request. The processed request is
validated and contains the requested documents.

The requested documents can be retrieved by the `ProcessedRequest.Success.requestedDocuments`
property. Each requested document contains the DocumentId that can be used to get the document
from the `DocumentManager`. This will allow the application to display the requested documents to
the user.

The `ProcessedRequest.Success.generateResponse(DisclosedDocuments, Algorithm?)` method is used to
generate the response. Each DisclosedDocument in the DisclosedDocuments object must contain the
DocumentId and the list of `DocItem`s, that are to be disclosed. There is a possibility that
requested document's keys, that are needed to sign the response. In this case, each
`DisclosedDocument` must contain the `keyUnlockData` property that defines the key unlocking.

Finally, the response is sent back to the verifier with the
`TransferManager.sendResponse(Response)`

The following example demonstrates how to handle the request and send the response.

```kotlin
transferManager.addTransferEventListener { event ->
    when (event) {
        is TransferEvent.RequestReceived -> try {
            // DeviceRequest assumed is received
            val processedRequest = event.processedRequest.getOrThrow() as ProcessedDeviceRequest
            // the request has been received and processed

            // the request processing was successful
            // requested documents can be shown in the application
            val requestedDocuments = processedRequest.requestedDocuments
            // display the requested documents so the user can confirm the disclosure

            // ...

            // Disclosed documents are constructed in ui.
            // If document keys to use for signing response is needed and require unlocking,
            // information about unlocking the keys should be passed to the disclosed document
            // for the corresponding document.
            // Here we use a software key unlock data to unlock the key, for the first requested document.
            val disclosedDocuments = DisclosedDocuments(
                DisclosedDocument(
                    requestedDocuments.first(),
                    keyUnlockData = SoftwareKeyUnlockData(passphrase = "passphrase_passed_from_ui")
                ),
            )
            // generate the response
            val response = processedRequest.generateResponse(
                disclosedDocuments = disclosedDocuments,
                signatureAlgorithm = Algorithm.ES256
            ).getOrThrow() as DeviceResponse

            transferManager.sendResponse(response)

        } catch (e: Throwable) {
            // An error occurred
            // handle the error
        }

        else -> {
            // ... rest of the events
        }
    }
}
```

## How to contribute

We welcome contributions to this project. To ensure that the process is smooth for everyone
involved, follow the guidelines found in [CONTRIBUTING.md](CONTRIBUTING.md).

## License

### Third-party component licenses

See [licenses.md](licenses.md) for details.

### License details

Copyright (c) 2023 European Commission

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
