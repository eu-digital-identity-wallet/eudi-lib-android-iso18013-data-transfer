//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManager](index.md)/[sendResponse](send-response.md)

# sendResponse

[androidJvm]\
abstract fun [sendResponse](send-response.md)(responseBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))

Sends response bytes to the connected reader To generate the response bytes, use
the [RequestProcessor.ProcessedRequest.Success.generateResponse](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/-processed-request/-success/generate-response.md)
method.

#### Parameters

androidJvm

| |
|---|
| responseBytes |
