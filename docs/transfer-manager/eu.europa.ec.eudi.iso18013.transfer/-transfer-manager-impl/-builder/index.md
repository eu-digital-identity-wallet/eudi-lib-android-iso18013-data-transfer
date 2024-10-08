//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferManagerImpl](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class for instantiating a [TransferManager](../../-transfer-manager/index.md) implementation

#### Parameters

androidJvm

|         |
|---------|
| context |

## Constructors

|                        |                                                                                                                              |
|------------------------|------------------------------------------------------------------------------------------------------------------------------|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Properties

| Name                                      | Summary                                                                                                                                                                                                                                                                                                               |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [documentManager](document-manager.md)    | [androidJvm]<br>var [documentManager](document-manager.md): DocumentManager?<br>document manager instance                                                                                                                                                                                                             |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>var [readerTrustStore](reader-trust-store.md): [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)?<br>reader trust store instance                                                                                                               |
| [retrievalMethods](retrieval-methods.md)  | [androidJvm]<br>var [retrievalMethods](retrieval-methods.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../../eu.europa.ec.eudi.iso18013.transfer.engagement/-device-retrieval-method/index.md)&gt;?<br>list of device retrieval methods |

## Functions

| Name                                      | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [build](build.md)                         | [androidJvm]<br>fun [build](build.md)(): [TransferManagerImpl](../index.md)<br>Build a [eu.europa.ec.eudi.iso18013.transfer.TransferManagerImpl](../index.md) instance with [DeviceRequestProcessor](../../../eu.europa.ec.eudi.iso18013.transfer.response.device/-device-request-processor/index.md) instance                                                                                                                           |
| [documentManager](document-manager.md)    | [androidJvm]<br>fun [documentManager](document-manager.md)(documentManager: DocumentManager): [TransferManagerImpl.Builder](index.md)<br>Document manager instance that will be used to retrieve the requested documents                                                                                                                                                                                                                 |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>fun [readerTrustStore](reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [TransferManagerImpl.Builder](index.md)<br>Reader trust store instance that will be used to verify the reader's certificate                                                                                                                   |
| [retrievalMethods](retrieval-methods.md)  | [androidJvm]<br>fun [retrievalMethods](retrieval-methods.md)(retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../../eu.europa.ec.eudi.iso18013.transfer.engagement/-device-retrieval-method/index.md)&gt;): [TransferManagerImpl.Builder](index.md)<br>Retrieval methods that will be used to retrieve the device request from the mdoc verifier |
