//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[DocRequest](index.md)

# DocRequest

[androidJvm]\
class [DocRequest](index.md)(val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val requestItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, val readerAuth: [ReaderAuth](../-reader-auth/index.md)?) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Represents a Device Request

## Constructors

| | |
|---|---|
| [DocRequest](-doc-request.md) | [androidJvm]<br>constructor(docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), requestItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, readerAuth: [ReaderAuth](../-reader-auth/index.md)?)<br>Create empty Doc request |

## Functions

| Name | Summary |
|---|---|
| [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document type e.g. eu.europa.ec.eudiw.pid.1 |
| [readerAuth](reader-auth.md) | [androidJvm]<br>val [readerAuth](reader-auth.md): [ReaderAuth](../-reader-auth/index.md)?<br>reader authentication [ReaderAuth](../-reader-auth/index.md) object if exist |
| [requestItems](request-items.md) | [androidJvm]<br>val [requestItems](request-items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;<br>a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of the requested document elements [DocItem](../-doc-item/index.md) (namespace and element identifier) |
