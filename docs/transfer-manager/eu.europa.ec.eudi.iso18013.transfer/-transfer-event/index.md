//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferEvent](index.md)

# TransferEvent

sealed interface [TransferEvent](index.md)

Transfer event

#### Inheritors

| |
|---|
| [QrEngagementReady](-qr-engagement-ready/index.md) |
| [Connecting](-connecting/index.md) |
| [Connected](-connected/index.md) |
| [RequestReceived](-request-received/index.md) |
| [ResponseSent](-response-sent/index.md) |
| [Redirect](-redirect/index.md) |
| [Disconnected](-disconnected/index.md) |
| [Error](-error/index.md) |

## Types

| Name                                               | Summary                                                                                                                                                                                                                                                                                                                                                                       |
|----------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Connected](-connected/index.md)                   | [androidJvm]<br>data object [Connected](-connected/index.md) : [TransferEvent](index.md)<br>Connected event. This event is triggered when the transfer is connected.                                                                                                                                                                                                          |
| [Connecting](-connecting/index.md)                 | [androidJvm]<br>data object [Connecting](-connecting/index.md) : [TransferEvent](index.md)<br>Connecting event. This event is triggered when the transfer is connecting.                                                                                                                                                                                                      |
| [Disconnected](-disconnected/index.md)             | [androidJvm]<br>data object [Disconnected](-disconnected/index.md) : [TransferEvent](index.md)<br>Disconnected event. This event is triggered when the transfer is disconnected.                                                                                                                                                                                              |
| [Error](-error/index.md)                           | [androidJvm]<br>data class [Error](-error/index.md)(val error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [TransferEvent](index.md)<br>Error event. This event is triggered when an error occurs.                                                                                                                              |
| [Listenable](-listenable/index.md)                 | [androidJvm]<br>interface [Listenable](-listenable/index.md)<br>Interface for events listenable                                                                                                                                                                                                                                                                               |
| [Listener](-listener/index.md)                     | [androidJvm]<br>fun interface [Listener](-listener/index.md)<br>Interface for transfer event listener                                                                                                                                                                                                                                                                         |
| [QrEngagementReady](-qr-engagement-ready/index.md) | [androidJvm]<br>data class [QrEngagementReady](-qr-engagement-ready/index.md)(val qrCode: [QrCode](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-qr-code/index.md)) : [TransferEvent](index.md)<br>Qr engagement ready event. This event is triggered when the QR code is ready to be displayed.                                                                      |
| [Redirect](-redirect/index.md)                     | [androidJvm]<br>data class [Redirect](-redirect/index.md)(val redirectUri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)) : [TransferEvent](index.md)<br>Redirect event. This event is triggered when the requires a redirect.                                                                                                                      |
| [RequestReceived](-request-received/index.md)      | [androidJvm]<br>data class [RequestReceived](-request-received/index.md)(val requestedDocumentData: [RequestedDocumentData](../-requested-document-data/index.md), val request: [Request](../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)) : [TransferEvent](index.md)<br>Request received event. This event is triggered when the request is received. |
| [ResponseSent](-response-sent/index.md)            | [androidJvm]<br>data object [ResponseSent](-response-sent/index.md) : [TransferEvent](index.md)<br>Response sent event. This event is triggered when the response is sent.                                                                                                                                                                                                    |
