# EUDI ISO 18013-5 Wallet Transfer library for Android

:heavy_exclamation_mark: **Important!** Before you proceed, please read
the [EUDI Wallet Reference Implementation project description](https://github.com/eu-digital-identity-wallet/.github-private/blob/main/profile/reference-implementation.md)

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

The released software is a initial development release version: 
-  The initial development release is an early endeavor reflecting the efforts of a short timeboxed period, and by no means can be considered as the final product.  
-  The initial development release may be changed substantially over time, might introduce new features but also may change or remove existing ones, potentially breaking compatibility with your existing code.
-  The initial development release is limited in functional scope.
-  The initial development release may contain errors or design flaws and other problems that could cause system or other failures and data loss.
-  The initial development release has reduced security, privacy, availability, and reliability standards relative to future releases. This could make the software slower, less reliable, or more vulnerable to attacks than mature software.
-  The initial development release is not yet comprehensively documented. 
-  Users of the software must perform sufficient engineering and additional testing in order to properly evaluate their application and determine whether any of the open-sourced components is suitable for use in that application.
-  We strongly recommend not putting this version of the software into production use.
-  Only the latest version of the software will be supported
  
## Requirements

- Android 8 (API level 26) or higher

### Dependencies

To include the library in your project, add the following dependencies to your app's build.gradle
file.

```groovy
dependencies {
    implementation "eu.europa.ec.eudi:eudi-lib-android-iso18013-data-transfer:0.2.0-SNAPSHOT"
    implementation "androidx.biometric:biometric-ktx:1.2.0-alpha05"
}
```

## How to Use

Below is a quick overview of how to use the library.

For source code documentation, see in [docs](docs/index.md) directory.

### Instantiating the TransferManager

The library provides a `TransferManager` class that can be used to transfer data.

To create a new instance of the `TransferManager` class, use
the `TransferManager.Builder` class:

```kotlin

import java.security.cert.X509Certificate
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.readerauth.ReaderTrustStore
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseGenerator
import eu.europa.ec.eudi.iso18013.transfer.retrieval.BleRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.retrieval.DocumentResolver
import eu.europa.ec.eudi.iso18013.transfer.retrieval.DocRequest
import eu.europa.ec.eudi.iso18013.transfer.retrieval.RetrievalMethod

val certificates = listOf<X509Certificate>(
    // put trusted reader certificates here
)
val readerTrustStore = ReaderTrustStore.getDefault(
    listOf(context.applicationContext.getCertificate(certificates))
)
val retrievalMethods = listOf<RetrievalMethod>(
    BleRetrievalMethod(
        peripheralServerMode = true,
        centralClientMode = true,
        clearBleCache = true
    )
)
val documentedResolver = DocumentResolver { docRequest: DocRequest ->
    // put your code here to resolve the document
    // usually document resolution is done based on `docRequest.docType`
}

// use the ResponseGenerator that parses the request and creates the response
// set it to the Transfer Manager
val deviceResponseGenerator = ResponseGenerator.Builder(context)
    .readerTrustStore(readerTrustStore)
    .documentsResolver(documentedResolver)
    .build()

val transferManager = TransferManager.Builder(context)
    .retrievalMethods(retrievalMethods)
    .responseGenerator(deviceResponseGenerator)
    .build()
``` 

### Attaching a TransferEvent.Listener

It is possible to attach a `TransferEvent.Listener` to the `TransferManager` to receive events.
The available events are:

1. `TransferEvent.QrEngagementReady`: The QR code is ready to be displayed. Get the QR code from
   `event.qrCode`.
2. `TransferEvent.Connecting`: The devices are connecting. Use this event to display a progress
   indicator.
3. `TransferEvent.Connected`: The devices are connected.
4. `TransferEvent.RequestReceived`: A request is received. Get the parsed request from `event.requestedDocumentData` 
   and the initial request as received by the verifier from `event.request`.
5. `TransferEvent.ResponseSent`: A response is sent.
6. `TransferEvent.Redirect`: This event prompts to redirect the user to the given Redirect URI. Get the Redirect URI from `event.redirectUri`.
7. `TransferEvent.Disconnected`: The devices are disconnected.
8. `TransferEvent.Error`: An error occurred. Get the `Throwable` error from `event.error`.

To receive events from the `TransferManager`, you must attach a `TransferEvent.Listener` to it:

The following example demonstrates how to implement a `TransferEvent.Listener` and attach it to the
transfer manager.

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
            when (val responseResult = responseGenerator.createResponse(disclosedDocuments)) {
                is ResponseResult.Failure -> {
                    // handle the failure
                }
                is ResponseResult.Success -> {
                    // send the response
                    transferManager.sendResponse(responseResult.response)
                }
                is ResponseResult.UserAuthRequired -> {
                    // user authentication is required. Get the crypto object from responseResult.cryptoObject
                    val cryptoObject = responseResult.cryptoObject
                }
            }
        }

        is TransferEvent.ResponseSent -> {
            // event when a response is sent
        }

        is TransferEvent.Disconnected -> {
            // event when the devices are disconnected
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
the `startQrEngagement()` method to start the QR code engagement.

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

To enable 18013-7 REST API functionality, declare to your app's manifest file (AndroidManifest.xml)
an Intent Filter for your MainActivity:

```xml

<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="mdoc" android:host="*" />
</intent-filter>
```

and set `launchMode="singleTask"` for this activity.

To initiate the transfer using an app link (reverse engagement), use
the `startEngagementToApp(Intent)` method.

The method receives as a parameter an `Intent` that contains the data for the device engagement.

The example below demonstrates how to use the `startEngagementToApp(Intent)` method to initiate the
device engagement and transfer.

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

In order to use NFC, you must create a service that extends `NfcEngagementService` and override
the `transferManager` property.

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

You can enable or disable the NFC device engagement in your app by calling the `enable()`
and `disable()` methods of the `NfcEngagementService` class.

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
Optionally, in the `enable()` method you can define your class that implements `NfcEngagementService`, e.g.:

```kotlin
 NfcEngagementService.enable(this, NfcEngagementServiceImpl::class.java)
```

This way, you can define the `NfcEngagementServiceImpl` service to be preferred while this activity is in the foreground.

### Receiving and sending response

When a request is received, the `TransferEvent.RequestReceived` event is triggered. The parsed request can
be retrieved from `event.requestedDocumentData`, while the initial request, as received by the verifier,
can be retrieved from `event.request`.

The parsed request contains a list of `RequestedDocument` objects, which can be used to show the user what
documents are requested. Also, a selectively disclosure option can be implemented using the
requested documents, so user can choose which of the documents to share.

Then, a `DisclosedDocuments` object must be created with the list of documents to be disclosed and
the response can be created using the `createResponse(DisclosedDocuments)` of `ResponseGenerator`.
The method returns a `ResponseResult` object, which can be one of the following:

1. `ResponseResult.Failure`: The response creation failed. The error can be retrieved from
   `responseResult.error`.
2. `ResponseResult.Success`: The response was created successfully. The response can be
   retrieved from `responseResult.response`.
3. `ResponseResult.UserAuthRequired`: The response creation requires user authentication. The
   `CryptoObject` can be retrieved from `responseResult.cryptoObject`. After success authentication
   the response can be created again, using the `TransferManager.createResponse(DisclosedDocuments)`
   method.

Then, the response can be send using the `TransferManager.sendResponse(response.deviceResponseBytes)`.

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
            when (val responseResult = transferManager.createResponse(disclosedDocuments)) {
                is ResponseResult.Failure -> {
                    // handle the failure
                }
                is ResponseResult.Response -> {
                    val response = responseResult.response
                    transferManager.sendResponse(response.deviceResponseBytes)
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
