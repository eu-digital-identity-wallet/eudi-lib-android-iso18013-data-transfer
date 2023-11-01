//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManager](index.md)

# TransferManager

[androidJvm]\
interface [TransferManager](index.md) : [TransferEvent.Listenable](../-transfer-event/-listenable/index.md)

Transfer manager

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))<br>Builder class for instantiating a [TransferManager](index.md) implementation |

## Functions

| Name | Summary |
|---|---|
| [addTransferEventListener](../-transfer-event/-listenable/add-transfer-event-listener.md) | [androidJvm]<br>abstract fun [addTransferEventListener](../-transfer-event/-listenable/add-transfer-event-listener.md)(listener: [TransferEvent.Listener](../-transfer-event/-listener/index.md)): [TransferEvent.Listenable](../-transfer-event/-listenable/index.md)<br>Add transfer event listener |
| [createResponse](create-response.md) | [androidJvm]<br>abstract fun [createResponse](create-response.md)(disclosedDocuments: [DisclosedDocuments](../-disclosed-documents/index.md)): [ResponseResult](../-response-result/index.md)<br>Create response |
| [removeAllTransferEventListeners](../-transfer-event/-listenable/remove-all-transfer-event-listeners.md) | [androidJvm]<br>abstract fun [removeAllTransferEventListeners](../-transfer-event/-listenable/remove-all-transfer-event-listeners.md)(): [TransferEvent.Listenable](../-transfer-event/-listenable/index.md)<br>Remove all transfer event listeners |
| [removeTransferEventListener](../-transfer-event/-listenable/remove-transfer-event-listener.md) | [androidJvm]<br>abstract fun [removeTransferEventListener](../-transfer-event/-listenable/remove-transfer-event-listener.md)(listener: [TransferEvent.Listener](../-transfer-event/-listener/index.md)): [TransferEvent.Listenable](../-transfer-event/-listenable/index.md)<br>Remove transfer event listener |
| [sendResponse](send-response.md) | [androidJvm]<br>abstract fun [sendResponse](send-response.md)(responseBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))<br>Send response |
| [setReaderTrustStore](set-reader-trust-store.md) | [androidJvm]<br>abstract fun [setReaderTrustStore](set-reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [TransferManager](index.md)<br>Set reader trust store |
| [setRetrievalMethods](set-retrieval-methods.md) | [androidJvm]<br>abstract fun [setRetrievalMethods](set-retrieval-methods.md)(retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../-device-retrieval-method/index.md)&gt;): [TransferManager](index.md)<br>Set retrieval methods |
| [setupNfcEngagement](setup-nfc-engagement.md) | [androidJvm]<br>abstract fun [setupNfcEngagement](setup-nfc-engagement.md)(service: [NfcEngagementService](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md)): [TransferManager](index.md)<br>Setup the [NfcEngagementService](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-nfc-engagement-service/index.md) Note: This method is only for internal use and should not be called by the app |
| [startEngagementToApp](start-engagement-to-app.md) | [androidJvm]<br>abstract fun [startEngagementToApp](start-engagement-to-app.md)(intent: [Intent](https://developer.android.com/reference/kotlin/android/content/Intent.html))<br>Starts the engagement to app, according to ISO 18013-7. |
| [startQrEngagement](start-qr-engagement.md) | [androidJvm]<br>abstract fun [startQrEngagement](start-qr-engagement.md)()<br>Starts the QR Engagement and generates the QR code |
| [stopPresentation](stop-presentation.md) | [androidJvm]<br>abstract fun [stopPresentation](stop-presentation.md)(sendSessionTerminationMessage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, useTransportSpecificSessionTermination: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)<br>Closes the connection and clears the data of the session |
