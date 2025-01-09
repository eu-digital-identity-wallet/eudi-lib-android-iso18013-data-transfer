//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManagerImpl](index.md)/[sendResponse](send-response.md)

# sendResponse

[androidJvm]\
open override fun [sendResponse](send-response.md)(response: [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md))

Sends the response bytes to the connected mdoc verifier To generate the response, use the [eu.europa.ec.eudi.iso18013.transfer.response.device.ProcessedDeviceRequest.generateResponse](../../eu.europa.ec.eudi.iso18013.transfer.response.device/-processed-device-request/generate-response.md) that is provided by the [eu.europa.ec.eudi.iso18013.transfer.TransferEvent.RequestReceived](../-transfer-event/-request-received/index.md) event.

#### Parameters

androidJvm

| | |
|---|---|
| response | the response to send |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if the response is not a [DeviceResponse](../../eu.europa.ec.eudi.iso18013.transfer.response.device/-device-response/index.md) |
