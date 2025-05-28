//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.readerauth](../index.md)/[ReaderTrustStoreImpl](index.md)/[validateCertificationTrustPath](validate-certification-trust-path.md)

# validateCertificationTrustPath

[androidJvm]\
open override fun [validateCertificationTrustPath](validate-certification-trust-path.md)(chainToDocumentSigner: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Validates the certification trust path of a document signer.

This function verifies the certificate chain against a set of trusted certificates and performs additional profile validation on the signer's certificate.

#### Return

`true` if the certification trust path is valid, `false` otherwise.

#### Parameters

androidJvm

| | |
|---|---|
| chainToDocumentSigner | The certificate chain leading to the document signer's certificate. |
