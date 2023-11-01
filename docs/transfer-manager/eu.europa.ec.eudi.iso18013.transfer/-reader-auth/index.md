//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[ReaderAuth](index.md)

# ReaderAuth

[androidJvm]\
class [ReaderAuth](index.md)(val readerAuth: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val readerSignIsValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val readerCertificateChain: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;, val readerCertificatedIsTrusted: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val readerCommonName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)

Reader authentication

## Constructors

| | |
|---|---|
| [ReaderAuth](-reader-auth.md) | [androidJvm]<br>constructor(readerAuth: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), readerSignIsValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), readerCertificateChain: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;, readerCertificatedIsTrusted: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), readerCommonName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>Create empty Reader auth |

## Functions

| Name | Summary |
|---|---|
| [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [describeContents](../-request-document/index.md#-1578325224%2FFunctions%2F-360525760)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isSuccess](is-success.md) | [androidJvm]<br>fun [isSuccess](is-success.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether the reader authentication is success (including that the signature of reader authentication is valid and reader auth certificate path is valid) |
| [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760) | [androidJvm]<br>abstract fun [writeToParcel](../-request-document/index.md#-1754457655%2FFunctions%2F-360525760)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [readerAuth](reader-auth.md) | [androidJvm]<br>val [readerAuth](reader-auth.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)<br>the reader authentication structure as CBOR encoded [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [readerCertificateChain](reader-certificate-chain.md) | [androidJvm]<br>val [readerCertificateChain](reader-certificate-chain.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;<br>reader auth certificate chain as [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of [X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html) |
| [readerCertificatedIsTrusted](reader-certificated-is-trusted.md) | [androidJvm]<br>val [readerCertificatedIsTrusted](reader-certificated-is-trusted.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>result of reader auth certificate path validation |
| [readerCommonName](reader-common-name.md) | [androidJvm]<br>val [readerCommonName](reader-common-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the Common Name (CN) field of the reader authentication certificate |
| [readerSignIsValid](reader-sign-is-valid.md) | [androidJvm]<br>val [readerSignIsValid](reader-sign-is-valid.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>indicates if the signature of reader authentication is valid |
