//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManagerImpl](index.md)/[sendResponse](send-response.md)

# sendResponse

[androidJvm]\
open override fun [sendResponse](send-response.md)(
responseBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))

Sends the response bytes to the connected mdoc verifier To generate the response bytes, use
the [RequestProcessor.ProcessedRequest.Success.generateResponse](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/-processed-request/-success/generate-response.md)
from the TransferManagerImpl.requestProcessor

#### Parameters

androidJvm

|               |                               |
|---------------|-------------------------------|
| responseBytes | the response bytes to be sent |
