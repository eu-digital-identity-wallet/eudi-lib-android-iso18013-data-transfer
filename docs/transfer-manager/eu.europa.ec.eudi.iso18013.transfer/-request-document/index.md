//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[RequestDocument](index.md)

# RequestDocument

[androidJvm]\
data class [RequestDocument](index.md)(val documentId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val docName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val userAuthentication: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val docRequest: [DocRequest](../-doc-request/index.md)) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Represents a request received by an mdoc verifier and contains the requested document data

## Constructors

| | |
|---|---|
| [RequestDocument](-request-document.md) | [androidJvm]<br>constructor(documentId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), docName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), userAuthentication: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), docRequest: [DocRequest](../-doc-request/index.md))<br>Create empty Request document data |

## Functions

| Name | Summary |
|---|---|
| [describeContents](index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](index.md#-1754457655%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [writeToParcel](index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [docName](doc-name.md) | [androidJvm]<br>val [docName](doc-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>is the name of document that is displayed to the user |
| [docRequest](doc-request.md) | [androidJvm]<br>val [docRequest](doc-request.md): [DocRequest](../-doc-request/index.md)<br>the document request [DocRequest](../-doc-request/index.md) |
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document type, e.g. eu.europa.ec.eudiw.pid.1 |
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the unique id of the document stored in identity credential api |
| [userAuthentication](user-authentication.md) | [androidJvm]<br>val [userAuthentication](user-authentication.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether user authentication is required to access the document |
