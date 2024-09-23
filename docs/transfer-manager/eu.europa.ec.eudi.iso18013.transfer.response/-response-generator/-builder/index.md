//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../../index.md)/[ResponseGenerator](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class for instantiating a [ResponseGenerator](../index.md) implementation

#### Parameters

androidJvm

| |
|---|
| context |

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Properties

| Name                                       | Summary                                                                                                                                                                  |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [documentsResolver](documents-resolver.md) | [androidJvm]<br>var [documentsResolver](documents-resolver.md): [DocumentsResolver](../../../eu.europa.ec.eudi.iso18013.transfer/-documents-resolver/index.md)?          |
| [readerTrustStore](reader-trust-store.md)  | [androidJvm]<br>var [readerTrustStore](reader-trust-store.md): [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [androidJvm]<br>fun [build](build.md)(): [DeviceResponseGeneratorImpl](../../-device-response-generator-impl/index.md)<br>Build a [ResponseGenerator](../index.md) instance |
| [documentResolver](document-resolver.md) | [androidJvm]<br>fun [documentResolver](document-resolver.md)(documentsResolver: [DocumentsResolver](../../../eu.europa.ec.eudi.iso18013.transfer/-documents-resolver/index.md)): [ResponseGenerator.Builder](index.md)<br>Document resolver that will be used to resolve the documents for the selective disclosure when creating the response |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>fun [readerTrustStore](reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [ResponseGenerator.Builder](index.md)<br>Reader trust store that will be used to validate the certificate chain of the mdoc verifier |
