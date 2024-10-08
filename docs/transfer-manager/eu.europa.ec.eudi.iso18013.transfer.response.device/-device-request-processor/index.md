//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response.device](../index.md)/[DeviceRequestProcessor](index.md)

# DeviceRequestProcessor

[androidJvm]\
class [DeviceRequestProcessor](index.md)(documentManager: DocumentManager, val
readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? =
null) : [RequestProcessor](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/index.md)

Implementation
of [RequestProcessor](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/index.md)
for [DeviceRequest](../-device-request/index.md) for the ISO 18013-5 standard.

## Constructors

|                                                        |                                                                                                                                                                                                |
|--------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DeviceRequestProcessor](-device-request-processor.md) | [androidJvm]<br>constructor(documentManager: DocumentManager, readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? = null) |

## Properties

| Name                                      | Summary                                                                                                                                                                                                                                 |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>val [readerTrustStore](reader-trust-store.md): [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? = null<br>the reader trust store to perform reader authentication |

## Functions

| Name                  | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [process](process.md) | [androidJvm]<br>open override fun [process](process.md)(request: [Request](../../eu.europa.ec.eudi.iso18013.transfer.response/-request/index.md)): [RequestProcessor.ProcessedRequest](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/-processed-request/index.md)<br>Process the [DeviceRequest](../-device-request/index.md) and return the [ProcessedDeviceRequest](../-processed-device-request/index.md) or a [RequestProcessor.ProcessedRequest.Failure](../../eu.europa.ec.eudi.iso18013.transfer.response/-request-processor/-processed-request/-failure/index.md). |
