//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../../index.md)/[ResponseResult](../index.md)/[Success](index.md)

# Success

[androidJvm]\
data class [Success](index.md)(val response: [Response](../../-response/index.md)) : [ResponseResult](../index.md)

The response generation was successful

## Constructors

| | |
|---|---|
| [Success](-success.md) | [androidJvm]<br>constructor(response: [Response](../../-response/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [response](response.md) | [androidJvm]<br>val [response](response.md): [Response](../../-response/index.md)<br>the response |

## Functions

| Name | Summary |
|---|---|
| [getOrNull](../get-or-null.md) | [androidJvm]<br>open fun [getOrNull](../get-or-null.md)(): [Response](../../-response/index.md)?<br>Returns the response or null |
| [getOrThrow](../get-or-throw.md) | [androidJvm]<br>open fun [getOrThrow](../get-or-throw.md)(): [Response](../../-response/index.md)<br>Returns the response or throws the throwable |
| [toKotlinResult](../../../eu.europa.ec.eudi.iso18013.transfer/to-kotlin-result.md) | [androidJvm]<br>fun [ResponseResult](../index.md).[toKotlinResult](../../../eu.europa.ec.eudi.iso18013.transfer/to-kotlin-result.md)(): [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[ResponseResult.Success](index.md)&gt;<br>Converts a [ResponseResult](../index.md) to a [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html) of [ResponseResult.Success](index.md) |
