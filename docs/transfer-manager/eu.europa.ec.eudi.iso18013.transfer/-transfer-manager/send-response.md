//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManager](index.md)/[sendResponse](send-response.md)

# sendResponse

[release]\
abstract fun [sendResponse](send-response.md)(response: [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md))

Sends response bytes to the connected reader and terminates the session.

**Note:** Currently, only a single request-response cycle per session is supported. Calling this method sends the response along with a session termination signal, ending the presentation session. To perform another exchange, a new session must be started.

To generate the response, use the [RequestProcessor.ProcessedRequest.Success.generateResponse](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/-processed-request/-success/generate-response.md) method.

#### Parameters

release

| | |
|---|---|
| response | The response to be sent |