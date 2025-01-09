//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[RequestedDocuments](index.md)

# RequestedDocuments

class [RequestedDocuments](index.md)(documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt;) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; 

Wrapper class that contains the requested documents

#### Parameters

androidJvm

| | |
|---|---|
| documents | the list of requested documents |

## Constructors

| | |
|---|---|
| [RequestedDocuments](-requested-documents.md) | [androidJvm]<br>constructor(vararg documents: [RequestedDocument](../-requested-document/index.md))<br>Constructor that takes a vararg of [RequestedDocument](../-requested-document/index.md) and converts it to a list<br>constructor(documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [size](index.md#844915858%2FProperties%2F-360525760) | [androidJvm]<br>open override val [size](index.md#844915858%2FProperties%2F-360525760): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [asMap](../../eu.europa.ec.eudi.iso18013.transfer/as-map.md) | [androidJvm]<br>@[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;docItemsToNameSpaces&quot;)<br>fun [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;.[asMap](../../eu.europa.ec.eudi.iso18013.transfer/as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;NameSpace, [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;ElementIdentifier&gt;&gt;<br>Converts a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of [DocItem](../-doc-item/index.md) to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) of NameSpace to [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of ElementIdentifier |
| [contains](index.md#-727319710%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [contains](index.md#-727319710%2FFunctions%2F-360525760)(element: [RequestedDocument](../-requested-document/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#1231303881%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [containsAll](index.md#1231303881%2FFunctions%2F-360525760)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#-903301123%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [forEach](index.md#-903301123%2FFunctions%2F-360525760)(p0: [Consumer](https://developer.android.com/reference/kotlin/java/util/function/Consumer.html)&lt;in [RequestedDocument](../-requested-document/index.md)&gt;) |
| [get](index.md#961975567%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [get](index.md#961975567%2FFunctions%2F-360525760)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [RequestedDocument](../-requested-document/index.md) |
| [indexOf](index.md#-2104361780%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [indexOf](index.md#-2104361780%2FFunctions%2F-360525760)(element: [RequestedDocument](../-requested-document/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.md#-1000881820%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [isEmpty](index.md#-1000881820%2FFunctions%2F-360525760)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#-1577986619%2FFunctions%2F-360525760) | [androidJvm]<br>open operator override fun [iterator](index.md#-1577986619%2FFunctions%2F-360525760)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [lastIndexOf](index.md#1583655938%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [lastIndexOf](index.md#1583655938%2FFunctions%2F-360525760)(element: [RequestedDocument](../-requested-document/index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](index.md#-236165689%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [listIterator](index.md#-236165689%2FFunctions%2F-360525760)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt;<br>open override fun [listIterator](index.md#845091493%2FFunctions%2F-360525760)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [parallelStream](index.md#-1592339412%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [parallelStream](index.md#-1592339412%2FFunctions%2F-360525760)(): [Stream](https://developer.android.com/reference/kotlin/java/util/stream/Stream.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [spliterator](index.md#703021258%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [spliterator](index.md#703021258%2FFunctions%2F-360525760)(): [Spliterator](https://developer.android.com/reference/kotlin/java/util/Spliterator.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [stream](index.md#135225651%2FFunctions%2F-360525760) | [androidJvm]<br>open fun [stream](index.md#135225651%2FFunctions%2F-360525760)(): [Stream](https://developer.android.com/reference/kotlin/java/util/stream/Stream.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [subList](index.md#423386006%2FFunctions%2F-360525760) | [androidJvm]<br>open override fun [subList](index.md#423386006%2FFunctions%2F-360525760)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[RequestedDocument](../-requested-document/index.md)&gt; |
| [toArray](index.md#-1215154575%2FFunctions%2F-360525760) | [androidJvm]<br>open fun &lt;[T](index.md#-1215154575%2FFunctions%2F-360525760) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [~~toArray~~](index.md#-1215154575%2FFunctions%2F-360525760)(p0: [IntFunction](https://developer.android.com/reference/kotlin/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-360525760)&gt;&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-360525760)&gt; |
