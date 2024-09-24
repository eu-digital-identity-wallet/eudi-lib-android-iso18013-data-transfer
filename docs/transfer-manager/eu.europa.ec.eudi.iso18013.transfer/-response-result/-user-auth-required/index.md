//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[ResponseResult](../index.md)/[UserAuthRequired](index.md)

# UserAuthRequired

[androidJvm]\
data class [UserAuthRequired](index.md)
&lt;[R](index.md) : [Response](../../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)
&gt;(val
cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) : [ResponseResult](../index.md)
&lt;[R](index.md)&gt;

User authentication is required to proceed with the response generation

## Constructors

| | |
|---|---|
| [UserAuthRequired](-user-auth-required.md) | [androidJvm]<br>constructor(cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) |

## Properties

| Name                             | Summary                                                                                                                                                                                                         |
|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [cryptoObject](crypto-object.md) | [androidJvm]<br>val [cryptoObject](crypto-object.md): [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?<br>the crypto object |
