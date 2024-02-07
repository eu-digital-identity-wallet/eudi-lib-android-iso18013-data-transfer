//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferManager](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class for instantiating a [TransferManager](../index.md) implementation

#### Parameters

androidJvm

| |
|---|
| context |

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [androidJvm]<br>fun [build](build.md)(): [TransferManager](../index.md)<br>Build a [TransferManager](../index.md) instance |
| [responseGenerator](response-generator.md) | [androidJvm]<br>fun [responseGenerator](response-generator.md)(responseGenerator: [DeviceResponseGeneratorImpl](../../../eu.europa.ec.eudi.iso18013.transfer.response/-device-response-generator-impl/index.md)): [TransferManager.Builder](index.md)<br>Response generator that will be parse the request and will create the response |
| [retrievalMethods](retrieval-methods.md) | [androidJvm]<br>fun [retrievalMethods](retrieval-methods.md)(retrievalMethods: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../-device-retrieval-method/index.md)&gt;): [TransferManager.Builder](index.md)<br>Retrieval methods that will be used to retrieve the device request from the mdoc verifier |

## Properties

| Name | Summary |
|---|---|
| [responseGenerator](response-generator.md) | [androidJvm]<br>var [responseGenerator](response-generator.md): [ResponseGenerator](../../../eu.europa.ec.eudi.iso18013.transfer.response/-response-generator/index.md)&lt;[DeviceRequest](../../../eu.europa.ec.eudi.iso18013.transfer.response/-device-request/index.md)&gt;? |
| [retrievalMethods](retrieval-methods.md) | [androidJvm]<br>var [retrievalMethods](retrieval-methods.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DeviceRetrievalMethod](../../-device-retrieval-method/index.md)&gt;? |
