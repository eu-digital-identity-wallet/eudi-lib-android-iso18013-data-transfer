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

- The initial development release is an early endeavor reflecting the efforts of a short timeboxed period, and by no
  means can be considered as the final product.
- The initial development release may be changed substantially over time, might introduce new features but also may
  change or remove existing ones, potentially breaking compatibility with your existing code.
- The initial development release is limited in functional scope.
- The initial development release may contain errors or design flaws and other problems that could cause system or other
  failures and data loss.
- The initial development release has reduced security, privacy, availability, and reliability standards relative to
  future releases. This could make the software slower, less reliable, or more vulnerable to attacks than mature
  software.
- The initial development release is not yet comprehensively documented.
- Users of the software must perform sufficient engineering and additional testing in order to properly evaluate their
  application and determine whether any of the open-sourced components is suitable for use in that application.
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
    implementation "eu.europa.ec.eudi:eudi-lib-android-iso18013-data-transfer:0.2.0"
    implementation "androidx.biometric:biometric-ktx:1.2.0-alpha05"
}
```

## How to Use

Below is a quick overview of how to use the library.

For source code documentation, see in the [docs](docs/index.md) directory.

### Instantiating the TransferManager

The library provides
a [`TransferManager`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/index.md)
implementation that can be used to transfer data.

To create a new instance of
the [`TransferManager`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/index.md) interface,
use
the [`TransferManager.Builder`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/-builder/index.md)
class:

```kotlin
import eu.europa.ec.eudi.iso18013.transfer.DeviceRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.DocumentsResolver
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseGenerator
import eu.europa.ec.eudi.iso18013.transfer.retrieval.BleRetrievalMethod
import java.security.cert.X509Certificate

val certificates = listOf<X509Certificate>(
    // put trusted reader certificates here
)
val readerTrustStore = ReaderTrustStore.getDefault(certificates)

val retrievalMethods = listOf<DeviceRetrievalMethod>(
    BleRetrievalMethod(
        peripheralServerMode = true,
        centralClientMode = true,
        clearBleCache = true
    )
)
val documentResolver = DocumentsResolver { docRequest: DocRequest ->
    // place your code here to resolve documents, 
    // usually document resolution is done based on `docRequest.docType`
}

// use the ResponseGenerator that parses the request and creates the response
// set it to the Transfer Manager
val deviceResponseGenerator = ResponseGenerator.Builder(context)
    .readerTrustStore(readerTrustStore)
    .documentResolver(documentResolver)
    .build()

val transferManager = TransferManager.Builder(context)
    .retrievalMethods(retrievalMethods)
    .responseGenerator(deviceResponseGenerator)
    .build()
``` 

### Attaching a TransferEvent.Listener

It is possible to attach
a [`TransferEvent.Listener`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-listener/index.md)
to the [`TransferManager`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/index.md) to
receive events.
The available events are:

1. [`TransferEvent.QrEngagementReady`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-qr-engagement-ready/index.md):
   The QR code is ready to be displayed. Get the QR code from
   [`TransferEvent.QrEngagementReady.qrCode`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-qr-engagement-ready/qr-code.md).
2. [`TransferEvent.Connecting`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-connecting/index.md):
   The devices are connecting. Use this event to display a progress
   indicator.
3. [`TransferEvent.Connected`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-connected/index.md):
   The devices are connected.
4. [`TransferEvent.RequestReceived`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-request-received/index.md):
   A request is received. Get the parsed request from `event.requestedDocumentData`
   and the initial request as received by the verifier
   from [`TransferEvent.RequestReceived.request`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-request-received/request.md).
5. [`TransferEvent.ResponseSent`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-response-sent/index.md):
   A response is sent.
6. [`TransferEvent.Redirect`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-redirect/index.md):
   This event prompts to redirect the user to the given Redirect URI. Get the Redirect URI
   from [`TransferEvent.Redirect.redirectUri`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-redirect/redirect-uri.md).
7. [`TransferEvent.Disconnected`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-disconnected/index.md):
   The devices are disconnected.
8. [`TransferEvent.Error`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-error/index.md):
   An error occurred. Get the `Throwable` error
   from [`TransferEvent.Error.error`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-error/-error.md).

The following example demonstrates how to implement
a [`TransferEvent.Listener`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-listener/index.md)
and attach it to
the [`TransferManager`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/index.md).

```kotlin

import android.media.MediaCodec.QueueRequest
import eu.europa.ec.eudi.iso18013.transfer.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.TransferEvent
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.engagement.QrCode

val transferEventListener = TransferEvent.Listener { event ->
    when (event) {
        is TransferEvent.QrEngagementReady -> {
            // event when the qr code is ready to be displayed. Get the qr code from event.qrCode
        }

        is TransferEvent.Connected -> {
            // event when the devices are connected
        }

        is TransferEvent.RequestReceived -> {
            // event when a request is received. 
            // Get the parsed request from event.requestedDocumentData

            val disclosedDocuments = DisclosedDocuments(
                listOf(
                    // add the disclosed documents here
                )
            )

            // create the response providing the disclosed documents
            when (val responseResult = transferManager.responseGenerator.createResponse(disclosedDocuments)) {
                is ResponseResult.Failure -> {
                    // handle the failure
                }
                is ResponseResult.Success -> {
                    // send the response
                    transferManager.sendResponse(
                        (responseResult.response as DeviceResponse).deviceResponseBytes
                    )
                }
                is ResponseResult.UserAuthRequired -> {
                    // User authentication is required. Get the crypto object from responseResult.cryptoObject
                    val cryptoObject = responseResult.cryptoObject
                }
            }
        }

        is TransferEvent.ResponseSent -> {
            // event when a response is sent
        }

        is TransferEvent.Disconnected -> {
            // event when the devices are disconnected, 
            // presentation can be stopped here
            transferManager.stopPresentation()
        }
        is TransferEvent.Error -> {
            // event when an error occurs. Get the error from event.error
            val error: Throwable = event.error
            // handle error 
            // stop presentation
            transferManager.stopPresentation()
        }
    }
}

transferManager.addTransferEventListener(transferEventListener)

```

### Initiating transfer

Transfer Manager provides the following methods for enabling device engagement and initiating the
transfer:

1. Scanning QR code
2. Reverse engagement using an app link
3. Using NFC .

These engagement methods offer seamless device pairing and data transfer initiation.

#### Using QR Code

Once the transfer manager is created and a transfer event listener is attached, use
the [`TransferManager.startQrEngagement`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/start-qr-engagement.md)`
method to start the QR code engagement.

```kotlin
transferManager.startQrEngagement()

//... other code

// in event listener when the qr code is ready to be displayed
when (event) {
    is TransferEvent.QrEngagementReady -> {
        // show the qr code to the user
        val qrCode: QrCode = event.qrCode
        val qrBitmap = qrCode.asBitmap(/* size */) // get the qr code as bitmap
        // - or -
        val qrView = qrCode.asView(/* context, width */) // get the qr code as view
    }
    // ... rest of the code
}
```

#### Using App Link

To enable ISO 18013-7 REST API functionality, declare to your app's manifest file (AndroidManifest.xml)
an Intent Filter for your MainActivity:

```xml

<intent-filter>
    <action android:name="android.intent.action.VIEW"/>
    <category android:name="android.intent.category.DEFAULT"/>
    <category android:name="android.intent.category.BROWSABLE"/>
    <data android:scheme="mdoc" android:host="*"/>
</intent-filter>
```

and set `launchMode="singleTask"` for this activity.

To initiate the transfer using an app link (reverse engagement), use
the [`TransferManager.startEngagementToApp(Intent)`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/start-engagement-to-app.md)
method.

The method receives as a parameter an `Intent` that contains the data for the device engagement.

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

To use NFC, you must create a service that
extends [`NfcEngagementService`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md)
and override
the  [`NfcEngagementService.transferManager`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/transfer-manager.md)
property.

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
            <action android:name="android.nfc.action.NDEF_DISCOVERED"/>
            <action android:name="android.nfc.cardemulation.action.HOST_APDU_SERVICE"/>
        </intent-filter>

        <!-- the following "@xml/nfc_engagement_apdu_service" in meta-data is provided by the library -->
        <meta-data android:name="android.nfc.cardemulation.host_apdu_service"
                   android:resource="@xml/nfc_engagement_apdu_service"/>
    </service>

</application>
```

You can enable or disable the NFC device engagement in your app by calling
the [`NfcEngagementService.enable()`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/-companion/enable.md)
and [`NfcEngagementService.disable()`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/-companion/disable.md)
methods.

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

Optionally,
in
the [`NfcEngagementService.enable()`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/-companion/enable.md)
method you can define your class
that
implements [`NfcEngagementService`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md).

For example:

```kotlin
 NfcEngagementService.enable(this, NfcEngagementServiceImpl::class.java)
```

This way, you can define the `NfcEngagementServiceImpl` service to be preferred while this activity is in the
foreground.

### Receiving a request and sending a response

When a request is received,
the [`TransferEvent.RequestReceived`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-request-received/index.md)
event is triggered. The parsed request can
be retrieved
from [`TransferEvent.RequestReceived.requestedDocumentData`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-request-received/requested-document-data.md),
while the initial request, as received by the verifier,
can be retrieved
from [`TransferEvent.RequestReceived.request`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-event/-request-received/request.md).

The parsed request contains a list
of [`RequestDocument`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-request-document) objects, which can
be used to show the user what
documents are requested. Also, a selective disclosure option can be implemented using the
requested documents, so the user can choose which of the documents to share.

Then [`DisclosedDocuments`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-disclosed-documents) must be
created that contains the documents to include in the response. After that, use the
[`TransferManager.responseGenerator.createResponse(DisclosedDocuments)`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/create-response.md)
method.

The method returns
a [`ResponseResult`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/index.md) object, which
can be one of the following:

1. [`ResponseResult.Failure`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-failure/index.md):
   The response creation failed. The error can be retrieved
   from [`ResponseResult.Failure.throwable`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-failure/throwable.md).
2. [`ResponseResult.Success`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-success/index.md):
   The response was created successfully. The response can be
   retrieved
   from [`ResponseResult.Success.response`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-success/response.md)
3. [`ResponseResult.UserAuthRequired`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-user-auth-required/index.md):
   The response creation requires user authentication. The
   `CryptoObject` can be retrieved
   from [`ResponseResult.UserAuthRequired.cryptoObject`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-response-result/-user-auth-required/crypto-object.md).
   After success authentication the response can be created again,
   using [`TransferManager.responseGenerator.createResponse(DisclosedDocuments)`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/create-response.md)
   method.

Send the response bytes using
the [`TransferManager.sendResponse(ByteArray)`](docs/transfer-manager/eu.europa.ec.eudi.iso18013.transfer/-transfer-manager/send-response.md)
method.

The following example demonstrates the above steps:

```kotlin

val transferEventListener = TransferEvent.Listener { event ->
    when (event) {

        is TransferEvent.RequestReceived -> {
            // event when a request is received. Get the request from event.request
            // use the received request to generate the appropriate response

            val disclosedDocuments = DisclosedDocuments(
                listOf(
                    // add the disclosed documents here
                )
            )
            when (val responseResult = transferManager.responseGenerator.createResponse(disclosedDocuments)) {
                is ResponseResult.Failure -> {
                    // handle the failure
                }
                is ResponseResult.Response -> {
                    val response = responseResult.response
                    transferManager.sendResponse((response as DeviceResponse).deviceResponseBytes)
                }
                is ResponseResult.UserAuthRequired -> {
                    // user authentication is required. Get the crypto object from responseResult.cryptoObject
                    val cryptoObject = responseResult.cryptoObject
                }
            }
        }
        // handle other events
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
