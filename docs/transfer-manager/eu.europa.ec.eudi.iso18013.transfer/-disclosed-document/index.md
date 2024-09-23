//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[DisclosedDocument](index.md)

# DisclosedDocument

[androidJvm]\
class [DisclosedDocument](index.md)(val documentId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val selectedDocItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, val docRequest: [DocRequest](../-doc-request/index.md)) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Represents a response that contains the document data that will be sent to an mdoc verifier

## Constructors

| | |
|---|---|
| [DisclosedDocument](-disclosed-document.md) | [androidJvm]<br>constructor(documentId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), selectedDocItems: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;, docRequest: [DocRequest](../-doc-request/index.md))<br>Create empty Response document data |

## Properties

| Name | Summary |
|---|---|
| [docRequest](doc-request.md) | [androidJvm]<br>val [docRequest](doc-request.md): [DocRequest](../-doc-request/index.md)<br>the received document request |
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document type, e.g. eu.europa.ec.eudiw.pid.1 |
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the unique id of the document stored in identity credential api |
| [selectedDocItems](selected-doc-items.md) | [androidJvm]<br>val [selectedDocItems](selected-doc-items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocItem](../-doc-item/index.md)&gt;<br>a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) that contains the document items [DocItem](../-doc-item/index.md), i.e the namespaces and the data elements that will be sent in the device response after selective disclosure |

## Functions

| Name                                                                                   | Summary                                                                                                                                                                                                                                                                               |
|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)                                                                                     |
| [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)    | [androidJvm]<br>abstract fun [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
