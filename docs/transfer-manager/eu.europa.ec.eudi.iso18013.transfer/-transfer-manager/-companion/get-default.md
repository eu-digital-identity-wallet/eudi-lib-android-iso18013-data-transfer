//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferManager](../index.md)/[Companion](index.md)/[getDefault](get-default.md)

# getDefault

[androidJvm]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)

fun [getDefault](get-default.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html),
documentManager: DocumentManager,
readerTrustStore: [ReaderTrustStore](../../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)? =
null,
retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)
&lt;[DeviceRetrievalMethod](../../../eu.europa.ec.eudi.iso18013.transfer.engagement/-device-retrieval-method/index.md)
&gt;? = null): [TransferManager](../index.md)

Create a new instance of [TransferManager](../index.md) for the ISO 18013-5 and ISO 18013-7
standards.

#### Return

a [TransferManagerImpl](../../-transfer-manager-impl/index.md)

#### Parameters

androidJvm

|                  |
|------------------|
| context          |
| documentManager  |
| readerTrustStore |
| retrievalMethods |
