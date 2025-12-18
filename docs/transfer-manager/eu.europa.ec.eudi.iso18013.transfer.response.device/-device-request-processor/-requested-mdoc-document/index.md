//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response.device](../../index.md)/[DeviceRequestProcessor](../index.md)/[RequestedMdocDocument](index.md)

# RequestedMdocDocument

[androidJvm]\
data class [RequestedMdocDocument](index.md)(val docType: &lt;Error class: unknown class&gt;, val requested: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;, val readerAuthentication: () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?, val matchedZkSystem: [MatchedZkSystem](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-matched-zk-system/index.md)? = null)

Parsed requested document.

## Constructors

| | |
|---|---|
| [RequestedMdocDocument](-requested-mdoc-document.md) | [androidJvm]<br>constructor(docType: &lt;Error class: unknown class&gt;, requested: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;, readerAuthentication: () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?, matchedZkSystem: [MatchedZkSystem](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-matched-zk-system/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): &lt;Error class: unknown class&gt;<br>the document type |
| [matchedZkSystem](matched-zk-system.md) | [androidJvm]<br>val [matchedZkSystem](matched-zk-system.md): [MatchedZkSystem](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-matched-zk-system/index.md)? = null<br>the matched zero-knowledge proof system and its specification, if any |
| [readerAuthentication](reader-authentication.md) | [androidJvm]<br>val [readerAuthentication](reader-authentication.md): () -&gt; [ReaderAuth](../../../eu.europa.ec.eudi.iso18013.transfer.response/-reader-auth/index.md)?<br>the reader authentication |
| [requested](requested.md) | [androidJvm]<br>val [requested](requested.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;&lt;Error class: unknown class&gt;, [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)&gt;&gt;<br>the requested elements |
