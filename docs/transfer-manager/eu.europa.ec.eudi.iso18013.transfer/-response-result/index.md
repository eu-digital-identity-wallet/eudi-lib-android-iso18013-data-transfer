//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[ResponseResult](index.md)

# ResponseResult

sealed interface [ResponseResult](index.md)

#### Inheritors

| |
|---|
| [UserAuthRequired](-user-auth-required/index.md) |
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name | Summary |
|---|---|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [ResponseResult](index.md) |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val response: [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)) : [ResponseResult](index.md) |
| [UserAuthRequired](-user-auth-required/index.md) | [androidJvm]<br>data class [UserAuthRequired](-user-auth-required/index.md)(val cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) : [ResponseResult](index.md) |
