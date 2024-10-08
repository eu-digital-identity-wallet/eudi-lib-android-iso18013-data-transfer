//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../../index.md)/[RequestProcessor](../index.md)/[ProcessedRequest](index.md)

# ProcessedRequest

sealed interface [ProcessedRequest](index.md)

Represents the result of a processed request

#### Inheritors

|                              |
|------------------------------|
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name                         | Summary                                                                                                                                                                                                                                     |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val error: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [RequestProcessor.ProcessedRequest](index.md)<br>The request processing failed |
| [Success](-success/index.md) | [androidJvm]<br>abstract class [Success](-success/index.md)(val requestedDocuments: [RequestedDocuments](../../-requested-documents/index.md)) : [RequestProcessor.ProcessedRequest](index.md)<br>The request processing was successful.    |

## Functions

| Name                          | Summary                                                                                                                                                      |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [getOrNull](get-or-null.md)   | [androidJvm]<br>open fun [getOrNull](get-or-null.md)(): [RequestProcessor.ProcessedRequest](index.md)?<br>Returns the processed request or null              |
| [getOrThrow](get-or-throw.md) | [androidJvm]<br>open fun [getOrThrow](get-or-throw.md)(): [RequestProcessor.ProcessedRequest](index.md)<br>Returns the processed request or throws the error |
