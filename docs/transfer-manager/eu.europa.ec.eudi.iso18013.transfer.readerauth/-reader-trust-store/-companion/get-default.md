//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.readerauth](../../index.md)/[ReaderTrustStore](../index.md)/[Companion](index.md)/[getDefault](get-default.md)

# getDefault

[androidJvm]\

@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)

fun [getDefault](get-default.md)(trustedCertificates: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;): [ReaderTrustStore](../index.md)

Returns a default trust store that uses the given list of trusted certificates.

#### Parameters

androidJvm

| | |
|---|---|
| trustedCertificates | the trusted certificates |
