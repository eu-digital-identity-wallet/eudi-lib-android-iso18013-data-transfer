//[transfer-manager](../../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../../../index.md)/[RequestProcessor](../../index.md)/[ProcessedRequest](../index.md)/[Failure](index.md)

# Failure

[androidJvm]\
data class [Failure](index.md)(val
error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [RequestProcessor.ProcessedRequest](../index.md)

The request processing failed

## Constructors

|                        |                                                                                                                            |
|------------------------|----------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure.md) | [androidJvm]<br>constructor(error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |

## Properties

| Name              | Summary                                                                                                                                    |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| [error](error.md) | [androidJvm]<br>val [error](error.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>the error |

## Functions

| Name                                                                                  | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|---------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [getOrNull](../get-or-null.md)                                                        | [androidJvm]<br>open fun [getOrNull](../get-or-null.md)(): [RequestProcessor.ProcessedRequest.Success](../-success/index.md)?<br>Returns the processed request or null                                                                                                                                                                                                                                                                                                                                                                            |
| [getOrThrow](../get-or-throw.md)                                                      | [androidJvm]<br>open fun [getOrThrow](../get-or-throw.md)(): [RequestProcessor.ProcessedRequest.Success](../-success/index.md)<br>Returns the processed request or throws the error                                                                                                                                                                                                                                                                                                                                                               |
| [toKotlinResult](../../../../eu.europa.ec.eudi.iso18013.transfer/to-kotlin-result.md) | [androidJvm]<br>fun [RequestProcessor.ProcessedRequest](../index.md).[toKotlinResult](../../../../eu.europa.ec.eudi.iso18013.transfer/to-kotlin-result.md)(): [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[RequestProcessor.ProcessedRequest.Success](../-success/index.md)&gt;<br>Converts a [RequestProcessor.ProcessedRequest](../index.md) to a [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html) of [RequestProcessor.ProcessedRequest.Success](../-success/index.md) |
