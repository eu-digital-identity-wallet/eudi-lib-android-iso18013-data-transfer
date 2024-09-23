//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[DisclosedDocuments](index.md)

# DisclosedDocuments

[androidJvm]\
data class [DisclosedDocuments](index.md)(val documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt;) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Represents a response that contains the document data that will be sent to an mdoc verifier

## Constructors

| | |
|---|---|
| [DisclosedDocuments](-disclosed-documents.md) | [androidJvm]<br>constructor(documents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt;) |

## Properties

| Name                      | Summary                                                                                                                                                                                             |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [documents](documents.md) | [androidJvm]<br>val [documents](documents.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DisclosedDocument](../-disclosed-document/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
