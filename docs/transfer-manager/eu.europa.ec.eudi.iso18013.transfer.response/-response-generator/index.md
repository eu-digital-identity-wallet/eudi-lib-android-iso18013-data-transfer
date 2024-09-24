//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[ResponseGenerator](index.md)

# ResponseGenerator

interface [ResponseGenerator](index.md)
&lt;in [RQ](index.md) : [Request](../-request/index.md), [RS](index.md) : [Response](../-response/index.md)
&gt;

Response Generator

#### Inheritors

| |
|---|
| [DeviceResponseGeneratorImpl](../-device-response-generator-impl/index.md) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))<br>Builder class for instantiating a [ResponseGenerator](index.md) implementation |

## Functions

| Name                                             | Summary                                                                                                                                                                                                                                                                                                                                                          |
|--------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createResponse](create-response.md)             | [androidJvm]<br>abstract fun [createResponse](create-response.md)(disclosedDocuments: [DisclosedDocuments](../../eu.europa.ec.eudi.iso18013.transfer/-disclosed-documents/index.md)): [ResponseResult](../../eu.europa.ec.eudi.iso18013.transfer/-response-result/index.md)&lt;[RS](index.md)&gt;<br>Create a response based on the disclosed documents          |
| [parseRequest](parse-request.md)                 | [androidJvm]<br>abstract fun [parseRequest](parse-request.md)(request: [RQ](index.md)): [RequestedDocumentData](../../eu.europa.ec.eudi.iso18013.transfer/-requested-document-data/index.md)<br>Parse the request and extract the requested document data                                                                                                        |
| [setReaderTrustStore](set-reader-trust-store.md) | [androidJvm]<br>abstract fun [setReaderTrustStore](set-reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [ResponseGenerator](index.md)&lt;[RQ](index.md), [RS](index.md)&gt;<br>Set the reader trust store. This is used to accomplish the reader authentication. |
