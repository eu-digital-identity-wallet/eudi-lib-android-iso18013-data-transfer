//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferEvent](index.md)

# TransferEvent

interface [TransferEvent](index.md)

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

| Name | Summary |
|---|---|
| [Connected](-connected/index.md) | [androidJvm]<br>object [Connected](-connected/index.md) : [TransferEvent](index.md) |
| [Connecting](-connecting/index.md) | [androidJvm]<br>object [Connecting](-connecting/index.md) : [TransferEvent](index.md) |
| [Disconnected](-disconnected/index.md) | [androidJvm]<br>object [Disconnected](-disconnected/index.md) : [TransferEvent](index.md) |
| [Error](-error/index.md) | [androidJvm]<br>data class [Error](-error/index.md)(val error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [TransferEvent](index.md) |
| [Listenable](-listenable/index.md) | [androidJvm]<br>interface [Listenable](-listenable/index.md) |
| [Listener](-listener/index.md) | [androidJvm]<br>fun interface [Listener](-listener/index.md) |
| [QrEngagementReady](-qr-engagement-ready/index.md) | [androidJvm]<br>data class [QrEngagementReady](-qr-engagement-ready/index.md)(val qrCode: [QrCode](../../eu.europa.ec.eudi.iso18013.transfer.engagement/-qr-code/index.md)) : [TransferEvent](index.md) |
| [Redirect](-redirect/index.md) | [androidJvm]<br>data class [Redirect](-redirect/index.md)(val redirectUri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)) : [TransferEvent](index.md) |
| [RequestReceived](-request-received/index.md) | [androidJvm]<br>data class [RequestReceived](-request-received/index.md)(val requestedDocumentData: [RequestedDocumentData](../-requested-document-data/index.md), val request: [Request](../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)) : [TransferEvent](index.md) |
| [ResponseSent](-response-sent/index.md) | [androidJvm]<br>object [ResponseSent](-response-sent/index.md) : [TransferEvent](index.md) |
