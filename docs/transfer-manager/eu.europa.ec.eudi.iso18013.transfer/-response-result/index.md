//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[ResponseResult](index.md)

# ResponseResult

sealed interface [ResponseResult](index.md)
&lt;out [R](index.md) : [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)
&gt;

Represents the result of a response

#### Inheritors

| |
|---|
| [UserAuthRequired](-user-auth-required/index.md) |
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name                                             | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure/index.md)                     | [androidJvm]<br>data class [Failure](-failure/index.md)&lt;[R](-failure/index.md) : [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)&gt;(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [ResponseResult](index.md)&lt;[R](-failure/index.md)&gt; <br>The response generation failed                                                                                                                                     |
| [Success](-success/index.md)                     | [androidJvm]<br>data class [Success](-success/index.md)&lt;[R](-success/index.md) : [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)&gt;(val response: [R](-success/index.md)) : [ResponseResult](index.md)&lt;[R](-success/index.md)&gt; <br>The response generation was successful                                                                                                                                                                                              |
| [UserAuthRequired](-user-auth-required/index.md) | [androidJvm]<br>data class [UserAuthRequired](-user-auth-required/index.md)&lt;[R](-user-auth-required/index.md) : [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)&gt;(val cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) : [ResponseResult](index.md)&lt;[R](-user-auth-required/index.md)&gt; <br>User authentication is required to proceed with the response generation |
