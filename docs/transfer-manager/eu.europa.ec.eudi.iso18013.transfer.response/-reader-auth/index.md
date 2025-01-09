//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[ReaderAuth](index.md)

# ReaderAuth

[androidJvm]\
data class [ReaderAuth](index.md)(val readerAuth: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val readerSignIsValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val readerCertificateChain: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;, val readerCertificatedIsTrusted: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val readerCommonName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Reader authentication

## Constructors

| | |
|---|---|
| [ReaderAuth](-reader-auth.md) | [androidJvm]<br>constructor(readerAuth: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), readerSignIsValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), readerCertificateChain: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;, readerCertificatedIsTrusted: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), readerCommonName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [isVerified](is-verified.md) | [androidJvm]<br>val [isVerified](is-verified.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>indicates if the reader authentication is verified |
| [readerAuth](reader-auth.md) | [androidJvm]<br>val [readerAuth](reader-auth.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)<br>the reader authentication structure as CBOR encoded [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [readerCertificateChain](reader-certificate-chain.md) | [androidJvm]<br>val [readerCertificateChain](reader-certificate-chain.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;<br>reader auth certificate chain as [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of [X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html) |
| [readerCertificatedIsTrusted](reader-certificated-is-trusted.md) | [androidJvm]<br>val [readerCertificatedIsTrusted](reader-certificated-is-trusted.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>result of reader auth certificate path validation |
| [readerCommonName](reader-common-name.md) | [androidJvm]<br>val [readerCommonName](reader-common-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the Common Name (CN) field of the reader authentication certificate |
| [readerSignIsValid](reader-sign-is-valid.md) | [androidJvm]<br>val [readerSignIsValid](reader-sign-is-valid.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>indicates if the signature of reader authentication is valid |
