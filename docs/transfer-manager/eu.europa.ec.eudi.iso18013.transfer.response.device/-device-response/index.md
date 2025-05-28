//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response.device](../index.md)/[DeviceResponse](index.md)

# DeviceResponse

[androidJvm]\
data class [DeviceResponse](index.md)(val deviceResponseBytes: [DeviceResponseBytes](../../eu.europa.ec.eudi.iso18013.transfer/-device-response-bytes/index.md), val sessionTranscriptBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val documentIds: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;DocumentId&gt;) : [Response](../../eu.europa.ec.eudi.iso18013.transfer.response/-response/index.md)

Represents a Device Response according to ISO 18013-5 standard.

## Constructors

| | |
|---|---|
| [DeviceResponse](-device-response.md) | [androidJvm]<br>constructor(deviceResponseBytes: [DeviceResponseBytes](../../eu.europa.ec.eudi.iso18013.transfer/-device-response-bytes/index.md), sessionTranscriptBytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), documentIds: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;DocumentId&gt;) |

## Properties

| Name | Summary |
|---|---|
| [deviceResponseBytes](device-response-bytes.md) | [androidJvm]<br>val [deviceResponseBytes](device-response-bytes.md): [DeviceResponseBytes](../../eu.europa.ec.eudi.iso18013.transfer/-device-response-bytes/index.md)<br>the device response bytes |
| [documentIds](document-ids.md) | [androidJvm]<br>val [documentIds](document-ids.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;DocumentId&gt;<br>the list of document ids in response indexed as positioned in CBOR array in responseBytes |
| [sessionTranscriptBytes](session-transcript-bytes.md) | [androidJvm]<br>val [sessionTranscriptBytes](session-transcript-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>the session transcript bytes |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
