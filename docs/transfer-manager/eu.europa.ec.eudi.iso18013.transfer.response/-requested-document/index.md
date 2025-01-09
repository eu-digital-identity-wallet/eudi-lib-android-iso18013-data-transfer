//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[RequestedDocument](index.md)

# RequestedDocument

[androidJvm]\
data class [RequestedDocument](index.md)(val documentId: DocumentId, val requestedItems: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [DocItem](../-doc-item/index.md), [IntentToRetain](../../eu.europa.ec.eudi.iso18013.transfer/-intent-to-retain/index.md)&gt;, val readerAuth: [ReaderAuth](../-reader-auth/index.md)?)

Represents a request received by a verifier and contains the requested documents and elements

## Constructors

| | |
|---|---|
| [RequestedDocument](-requested-document.md) | [androidJvm]<br>constructor(documentId: DocumentId, requestedItems: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [DocItem](../-doc-item/index.md), [IntentToRetain](../../eu.europa.ec.eudi.iso18013.transfer/-intent-to-retain/index.md)&gt;, readerAuth: [ReaderAuth](../-reader-auth/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): DocumentId<br>the unique id of the document |
| [readerAuth](reader-auth.md) | [androidJvm]<br>val [readerAuth](reader-auth.md): [ReaderAuth](../-reader-auth/index.md)?<br>the result of the reader authentication |
| [requestedItems](requested-items.md) | [androidJvm]<br>val [requestedItems](requested-items.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [DocItem](../-doc-item/index.md), [IntentToRetain](../../eu.europa.ec.eudi.iso18013.transfer/-intent-to-retain/index.md)&gt;<br>the list of requested items |
