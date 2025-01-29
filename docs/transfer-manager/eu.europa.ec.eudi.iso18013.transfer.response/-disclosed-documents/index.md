//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[DisclosedDocuments](index.md)

# DisclosedDocuments

class [DisclosedDocuments](index.md)(documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; = emptyList()) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; 

The list of disclosed documents, the documents and their elements that the holder has disclosed to the verifier.

#### Parameters

androidJvm

| | |
|---|---|
| documents | the list of disclosed documents |

## Constructors

| | |
|---|---|
| [DisclosedDocuments](-disclosed-documents.md) | [androidJvm]<br>constructor(vararg documents: [DisclosedDocument](../-disclosed-document/index.md))<br>Constructor for vararg DisclosedDocument parameters<br>constructor(documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; = emptyList()) |

## Properties

| Name | Summary |
|---|---|
| [size](../-requested-documents/index.md#844915858%2FProperties%2F-360525760) | [androidJvm]<br>open override val [size](../-requested-documents/index.md#844915858%2FProperties%2F-360525760): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [asMap](../../eu.europa.ec.eudi.iso18013.transfer/as-map.md) | [androidJvm]<br>@[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;docItemsToNameSpaces&quot;)<br>fun [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;.[asMap](../../eu.europa.ec.eudi.iso18013.transfer/as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;NameSpace, [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;ElementIdentifier&gt;&gt;<br>Converts a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html) of [DocItem](../-doc-item/index.md) to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html) of NameSpace to [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html) of ElementIdentifier |
| [contains](index.md#-350827050%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [contains](index.md#-350827050%2FFunctions%2F-360525760)(element: [DisclosedDocument](../-disclosed-document/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#17674453%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [containsAll](index.md#17674453%2FFunctions%2F-360525760)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#-2116930551%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [forEach](index.md#-2116930551%2FFunctions%2F-360525760)(p0: [Consumer](https://developer.android.com/reference/kotlin/java/util/function/Consumer.html)&lt;in [DisclosedDocument](../-disclosed-document/index.md)&gt;) |
| [get](../-requested-documents/index.md#961975567%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [get](../-requested-documents/index.md#961975567%2FFunctions%2F-360525760)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [DisclosedDocument](../-disclosed-document/index.md) |
| [indexOf](index.md#-1727869120%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [indexOf](index.md#-1727869120%2FFunctions%2F-360525760)(element: [DisclosedDocument](../-disclosed-document/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [isEmpty](../-requested-documents/index.md#-1000881820%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [isEmpty](../-requested-documents/index.md#-1000881820%2FFunctions%2F-360525760)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [iterator](../-requested-documents/index.md#-1577986619%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [iterator](../-requested-documents/index.md#-1577986619%2FFunctions%2F-360525760)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-iterator/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [lastIndexOf](index.md#1960148598%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [lastIndexOf](index.md#1960148598%2FFunctions%2F-360525760)(element: [DisclosedDocument](../-disclosed-document/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [listIterator](../-requested-documents/index.md#-236165689%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [listIterator](../-requested-documents/index.md#-236165689%2FFunctions%2F-360525760)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list-iterator/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt;<br>open override fun [listIterator](../-requested-documents/index.md#845091493%2FFunctions%2F-360525760)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list-iterator/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [parallelStream](../-requested-documents/index.md#-1592339412%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [parallelStream](../-requested-documents/index.md#-1592339412%2FFunctions%2F-360525760)(): [Stream](https://developer.android.com/reference/kotlin/java/util/stream/Stream.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [spliterator](../-requested-documents/index.md#703021258%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [spliterator](../-requested-documents/index.md#703021258%2FFunctions%2F-360525760)(): [Spliterator](https://developer.android.com/reference/kotlin/java/util/Spliterator.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [stream](../-requested-documents/index.md#135225651%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [stream](../-requested-documents/index.md#135225651%2FFunctions%2F-360525760)(): [Stream](https://developer.android.com/reference/kotlin/java/util/stream/Stream.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [subList](../-requested-documents/index.md#423386006%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [subList](../-requested-documents/index.md#423386006%2FFunctions%2F-360525760)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |
| [toArray](../-requested-documents/index.md#-1215154575%2FFunctions%2F-360525760) | [androidJvm]<br>open fun &lt;[T](../-requested-documents/index.md#-1215154575%2FFunctions%2F-360525760) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt; [~~toArray~~](../-requested-documents/index.md#-1215154575%2FFunctions%2F-360525760)(p0: [IntFunction](https://developer.android.com/reference/kotlin/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[T](../-requested-documents/index.md#-1215154575%2FFunctions%2F-360525760)&gt;&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[T](../-requested-documents/index.md#-1215154575%2FFunctions%2F-360525760)&gt; |
