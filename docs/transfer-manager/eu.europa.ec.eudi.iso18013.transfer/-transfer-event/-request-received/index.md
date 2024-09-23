//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferEvent](../index.md)/[RequestReceived](index.md)

# RequestReceived

[androidJvm]\
data class [RequestReceived](index.md)(val requestedDocumentData: [RequestedDocumentData](../../-requested-document-data/index.md), val request: [Request](../../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)) : [TransferEvent](../index.md)

Request received event. This event is triggered when the request is received.

## Constructors

| | |
|---|---|
| [RequestReceived](-request-received.md) | [androidJvm]<br>constructor(requestedDocumentData: [RequestedDocumentData](../../-requested-document-data/index.md), request: [Request](../../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)) |

## Properties

| Name                                                | Summary                                                                                                                                                                  |
|-----------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [request](request.md)                               | [androidJvm]<br>val [request](request.md): [Request](../../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)<br>the request                             |
| [requestedDocumentData](requested-document-data.md) | [androidJvm]<br>val [requestedDocumentData](requested-document-data.md): [RequestedDocumentData](../../-requested-document-data/index.md)<br>the requested document data |
