//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[DocItem](index.md)

# DocItem

[androidJvm]\
data class [DocItem](index.md)(val namespace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val elementIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Doc item represents a data element

## Constructors

| | |
|---|---|
| [DocItem](-doc-item.md) | [androidJvm]<br>constructor(namespace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), elementIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>Create empty Doc item |

## Properties

| Name | Summary |
|---|---|
| [elementIdentifier](element-identifier.md) | [androidJvm]<br>val [elementIdentifier](element-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the data element identifier e.g. family_name, given_name |
| [namespace](namespace.md) | [androidJvm]<br>val [namespace](namespace.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the namespace that the element belong to, e.g. eu.europa.ec.eudiw.pid.1 |

## Functions

| Name                                                                                   | Summary                                                                                                                                                                                                                                                                               |
|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)                                                                                     |
| [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)    | [androidJvm]<br>abstract fun [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
