//[transfer-manager](../../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../../../index.md)/[RequestProcessor](../../index.md)/[ProcessedRequest](../index.md)/[Success](index.md)

# Success

abstract class [Success](index.md)(val
requestedDocuments: [RequestedDocuments](../../../-requested-documents/index.md)) : [RequestProcessor.ProcessedRequest](../index.md)

The request processing was successful.

#### Inheritors

|                                                                                                                              |
|------------------------------------------------------------------------------------------------------------------------------|
| [ProcessedDeviceRequest](../../../../eu.europa.ec.eudi.iso18013.transfer.response.device/-processed-device-request/index.md) |

## Constructors

|                        |                                                                                                               |
|------------------------|---------------------------------------------------------------------------------------------------------------|
| [Success](-success.md) | [androidJvm]<br>constructor(requestedDocuments: [RequestedDocuments](../../../-requested-documents/index.md)) |

## Properties

| Name                                         | Summary                                                                                                                                                   |
|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| [requestedDocuments](requested-documents.md) | [androidJvm]<br>val [requestedDocuments](requested-documents.md): [RequestedDocuments](../../../-requested-documents/index.md)<br>the requested documents |

## Functions

| Name                                     | Summary                                                                                                                                                                                                                                                                                             |
|------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [generateResponse](generate-response.md) | [androidJvm]<br>abstract fun [generateResponse](generate-response.md)(disclosedDocuments: [DisclosedDocuments](../../../-disclosed-documents/index.md), signatureAlgorithm: Algorithm?): [ResponseResult](../../../-response-result/index.md)<br>Generates the response for the disclosed documents |
| [getOrNull](../get-or-null.md)           | [androidJvm]<br>open fun [getOrNull](../get-or-null.md)(): [RequestProcessor.ProcessedRequest](../index.md)?<br>Returns the processed request or null                                                                                                                                               |
| [getOrThrow](../get-or-throw.md)         | [androidJvm]<br>open fun [getOrThrow](../get-or-throw.md)(): [RequestProcessor.ProcessedRequest](../index.md)<br>Returns the processed request or throws the error                                                                                                                                  |
