//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[ResponseGenerator](index.md)

# ResponseGenerator

abstract class [ResponseGenerator](index.md)&lt;[T](index.md)&gt;

Response Generator

#### Inheritors

| |
|---|
| [DeviceResponseGeneratorImpl](../-device-response-generator-impl/index.md) |

## Constructors

| | |
|---|---|
| [ResponseGenerator](-response-generator.md) | [androidJvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))<br>Builder class for instantiating a [ResponseGenerator](index.md) implementation |

## Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | [androidJvm]<br>abstract fun [createResponse](create-response.md)(disclosedDocuments: [DisclosedDocuments](../../eu.europa.ec.eudi.iso18013.transfer/-disclosed-documents/index.md)): [ResponseResult](../../eu.europa.ec.eudi.iso18013.transfer/-response-result/index.md) |
| [parseRequest](parse-request.md) | [androidJvm]<br>abstract fun [parseRequest](parse-request.md)(request: [T](index.md)): [RequestedDocumentData](../../eu.europa.ec.eudi.iso18013.transfer/-requested-document-data/index.md) |
| [setReaderTrustStore](set-reader-trust-store.md) | [androidJvm]<br>abstract fun [setReaderTrustStore](set-reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [ResponseGenerator](index.md)&lt;[T](index.md)&gt; |
