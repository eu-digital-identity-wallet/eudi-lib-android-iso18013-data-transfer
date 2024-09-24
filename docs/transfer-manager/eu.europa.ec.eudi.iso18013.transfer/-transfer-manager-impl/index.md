//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManagerImpl](index.md)

# TransferManagerImpl

class [TransferManagerImpl](index.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), val
responseGenerator: [ResponseGenerator](../../eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/index.md)
&lt;[DeviceRequest](../../eu.europa.ec.eudi.iso18013.transfer.response/-device-request/index.md)
&gt;) : [TransferManager](../-transfer-manager/index.md)

Transfer Manager class used for performing device engagement and data retrieval.

#### Parameters

androidJvm

|                   |                                                                              |
|-------------------|------------------------------------------------------------------------------|
| context           | application context                                                          |
| responseGenerator | response generator instance that parses the request and creates the response |

## Constructors

|                                                  |                                                                                                                                                                                                                                                                                                                                                                                            |
|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [TransferManagerImpl](-transfer-manager-impl.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), responseGenerator: [ResponseGenerator](../../eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/index.md)&lt;[DeviceRequest](../../eu.europa.ec.eudi.iso18013.transfer.response/-device-request/index.md)&gt;)<br>Create empty Transfer manager |

## Types

| Name                             | Summary                                                 |
|----------------------------------|---------------------------------------------------------|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name                                       | Summary                                                                                                                                                                                                                                                                                |
|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [responseGenerator](response-generator.md) | [androidJvm]<br>open override val [responseGenerator](response-generator.md): [ResponseGenerator](../../eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/index.md)&lt;[DeviceRequest](../../eu.europa.ec.eudi.iso18013.transfer.response/-device-request/index.md)&gt; |

## Functions

| Name                                                                      | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|---------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [addTransferEventListener](add-transfer-event-listener.md)                | [androidJvm]<br>open override fun [addTransferEventListener](add-transfer-event-listener.md)(listener: [TransferEvent.Listener](../-transfer-event/-listener/index.md)): [TransferManagerImpl](index.md)<br>Add a transfer event listener                                                                                                                                                                                                          |
| [removeAllTransferEventListeners](remove-all-transfer-event-listeners.md) | [androidJvm]<br>open override fun [removeAllTransferEventListeners](remove-all-transfer-event-listeners.md)(): [TransferManagerImpl](index.md)<br>Remove all transfer event listeners                                                                                                                                                                                                                                                              |
| [removeTransferEventListener](remove-transfer-event-listener.md)          | [androidJvm]<br>open override fun [removeTransferEventListener](remove-transfer-event-listener.md)(listener: [TransferEvent.Listener](../-transfer-event/-listener/index.md)): [TransferManagerImpl](index.md)<br>Remove a transfer event listener                                                                                                                                                                                                 |
| [sendResponse](send-response.md)                                          | [androidJvm]<br>open override fun [sendResponse](send-response.md)(responseBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))<br>Send response                                                                                                                                                                                                                                                        |
| [setRetrievalMethods](set-retrieval-methods.md)                           | [androidJvm]<br>open override fun [setRetrievalMethods](set-retrieval-methods.md)(retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../-device-retrieval-method/index.md)&gt;): [TransferManagerImpl](index.md)<br>Set retrieval methods                                                                                                                       |
| [setupNfcEngagement](setup-nfc-engagement.md)                             | [androidJvm]<br>open override fun [setupNfcEngagement](setup-nfc-engagement.md)(service: [NfcEngagementService](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md)): [TransferManagerImpl](index.md)<br>Setup the [NfcEngagementService](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md) Note: This method is only for internal use and should not be called by the app |
| [startEngagementToApp](start-engagement-to-app.md)                        | [androidJvm]<br>open override fun [startEngagementToApp](start-engagement-to-app.md)(intent: [Intent](https://developer.android.com/reference/kotlin/android/content/Intent.html))<br>Starts the engagement to app, according to ISO 18013-7.                                                                                                                                                                                                      |
| [startQrEngagement](start-qr-engagement.md)                               | [androidJvm]<br>open override fun [startQrEngagement](start-qr-engagement.md)()<br>Starts the QR Engagement and generates the QR code                                                                                                                                                                                                                                                                                                              |
| [stopPresentation](stop-presentation.md)                                  | [androidJvm]<br>open override fun [stopPresentation](stop-presentation.md)(sendSessionTerminationMessage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), useTransportSpecificSessionTermination: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))<br>Closes the connection and clears the data of the session                                                              |
