//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[DisclosedDocument](index.md)

# DisclosedDocument

[androidJvm]\
data class [DisclosedDocument](index.md)(val documentId: DocumentId, val disclosedItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, val keyUnlockData: KeyUnlockData? = null)

Represents a response that contains the document data that will be sent to an mdoc verifier

## Constructors

| | |
|---|---|
| [DisclosedDocument](-disclosed-document.md) | [androidJvm]<br>constructor(requestedDocument: [RequestedDocument](../-requested-document/index.md), disclosedItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;? = null, keyUnlockData: KeyUnlockData? = null)<br>Alternative constructor that takes a [RequestedDocument](../-requested-document/index.md) and a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html) of [DocItem](../-doc-item/index.md) to create a [DisclosedDocument](index.md)<br>constructor(documentId: DocumentId, disclosedItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, keyUnlockData: KeyUnlockData? = null) |

## Properties

| Name | Summary |
|---|---|
| [disclosedItems](disclosed-items.md) | [androidJvm]<br>val [disclosedItems](disclosed-items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;<br>a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html) that contains the document items [DocItem](../-doc-item/index.md), i.e the namespaces and the data elements that will be sent in the device response after selective disclosure |
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): DocumentId<br>the unique id of the document |
| [keyUnlockData](key-unlock-data.md) | [androidJvm]<br>val [keyUnlockData](key-unlock-data.md): KeyUnlockData? = null<br>the key unlock data that will be used to unlock document's key for signing the response |
