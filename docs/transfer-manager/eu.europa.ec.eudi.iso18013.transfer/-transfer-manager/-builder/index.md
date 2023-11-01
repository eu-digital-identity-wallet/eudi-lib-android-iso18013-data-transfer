//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferManager](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class for instantiating a [TransferManager](../index.md) implementation

#### Parameters

androidJvm

| |
|---|
| context |

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [androidJvm]<br>fun [build](build.md)(): [TransferManager](../index.md)<br>Build a [TransferManager](../index.md) instance |
| [documentResolver](document-resolver.md) | [androidJvm]<br>fun [documentResolver](document-resolver.md)(documentsResolver: [DocumentsResolver](../../-documents-resolver/index.md)): [TransferManager.Builder](index.md)<br>Document resolver that will be used to resolve the documents for the selective disclosure when creating the response |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>fun [readerTrustStore](reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [TransferManager.Builder](index.md)<br>Reader trust store that will be used to validate the certificate chain of the mdoc verifier |
| [retrievalMethods](retrieval-methods.md) | [androidJvm]<br>fun [retrievalMethods](retrieval-methods.md)(retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../-device-retrieval-method/index.md)&gt;): [TransferManager.Builder](index.md)<br>Retrieval methods that will be used to retrieve the device request from the mdoc verifier |

## Properties

| Name | Summary |
|---|---|
| [documentsResolver](documents-resolver.md) | [androidJvm]<br>var [documentsResolver](documents-resolver.md): [DocumentsResolver](../../-documents-resolver/index.md)? |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>var [readerTrustStore](reader-trust-store.md): [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? |
| [retrievalMethods](retrieval-methods.md) | [androidJvm]<br>var [retrievalMethods](retrieval-methods.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../-device-retrieval-method/index.md)&gt;? |
