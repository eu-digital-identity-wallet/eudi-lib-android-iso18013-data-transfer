//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[DeviceResponseGeneratorImpl](index.md)

# DeviceResponseGeneratorImpl

class [DeviceResponseGeneratorImpl](index.md)(val context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentsResolver: [DocumentsResolver](../../eu.europa.ec.eudi.iso18013.transfer/-documents-resolver/index.md), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) : [ResponseGenerator](../-response-generator/index.md)&lt;[DeviceRequest](../-device-request/index.md)&gt; 

DeviceResponseGeneratorImpl class is used for parsing a DeviceRequest and generating the DeviceResponse

#### Parameters

androidJvm

| | |
|---|---|
| context | application context |
| documentsResolver | document manager instance |
| storageEngine | storage engine used to store documents |
| secureArea | secure area used to store documents' keys |

## Constructors

| | |
|---|---|
| [DeviceResponseGeneratorImpl](-device-response-generator-impl.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentsResolver: [DocumentsResolver](../../eu.europa.ec.eudi.iso18013.transfer/-documents-resolver/index.md), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) |

## Properties

| Name                  | Summary                                                                                                                           |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [context](context.md) | [androidJvm]<br>val [context](context.md): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html) |

## Functions

| Name | Summary |
|---|---|
| [createResponse](create-response.md) | [androidJvm]<br>open override fun [createResponse](create-response.md)(disclosedDocuments: [DisclosedDocuments](../../eu.europa.ec.eudi.iso18013.transfer/-disclosed-documents/index.md)): [ResponseResult](../../eu.europa.ec.eudi.iso18013.transfer/-response-result/index.md)<br>Creates a response and returns a ResponseResult |
| [parseRequest](parse-request.md) | [androidJvm]<br>open override fun [parseRequest](parse-request.md)(request: [DeviceRequest](../-device-request/index.md)): [RequestedDocumentData](../../eu.europa.ec.eudi.iso18013.transfer/-requested-document-data/index.md)<br>Parses a request and returns the requested document data |
| [readerTrustStore](reader-trust-store.md) | [androidJvm]<br>fun [readerTrustStore](reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [DeviceResponseGeneratorImpl](index.md)<br>Set a trust store so that reader authentication can be performed. |
| [setReaderTrustStore](set-reader-trust-store.md) | [androidJvm]<br>open override fun [setReaderTrustStore](set-reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [DeviceResponseGeneratorImpl](index.md)<br>Set a trust store so that reader authentication can be performed. |
