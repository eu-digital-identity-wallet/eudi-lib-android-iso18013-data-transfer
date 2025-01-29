//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response.device](../../index.md)/[DeviceRequestProcessor](../index.md)/[RequestedMdocDocument](index.md)

# RequestedMdocDocument

[androidJvm]\
data class [RequestedMdocDocument](index.md)(val docType: DocType, val requested: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;NameSpace, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;ElementIdentifier, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;, val readerAuthentication: () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?)

Parsed requested document.

## Constructors

| | |
|---|---|
| [RequestedMdocDocument](-requested-mdoc-document.md) | [androidJvm]<br>constructor(docType: DocType, requested: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;NameSpace, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;ElementIdentifier, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;, readerAuthentication: () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): DocType<br>the document type |
| [readerAuthentication](reader-authentication.md) | [androidJvm]<br>val [readerAuthentication](reader-authentication.md): () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?<br>the reader authentication |
| [requested](requested.md) | [androidJvm]<br>val [requested](requested.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;NameSpace, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;ElementIdentifier, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;<br>the requested elements |
