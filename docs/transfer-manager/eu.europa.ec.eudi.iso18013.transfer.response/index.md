//[transfer-manager](../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [DeviceRequest](-device-request/index.md) | [androidJvm]<br>class [DeviceRequest](-device-request/index.md)(val deviceRequestBytes: [DeviceRequestBytes](index.md#-1826744921%2FClasslikes%2F-360525760), val sessionTranscriptBytes: [SessionTranscriptBytes](index.md#-274759174%2FClasslikes%2F-360525760)) : [Request](-request/index.md) |
| [DeviceRequestBytes](index.md#-1826744921%2FClasslikes%2F-360525760) | [androidJvm]<br>typealias [DeviceRequestBytes](index.md#-1826744921%2FClasslikes%2F-360525760) = [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [DeviceResponse](-device-response/index.md) | [androidJvm]<br>class [DeviceResponse](-device-response/index.md)(val deviceResponseBytes: [DeviceResponseBytes](index.md#-1761425419%2FClasslikes%2F-360525760)) : [Response](-response/index.md) |
| [DeviceResponseBytes](index.md#-1761425419%2FClasslikes%2F-360525760) | [androidJvm]<br>typealias [DeviceResponseBytes](index.md#-1761425419%2FClasslikes%2F-360525760) = [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [DeviceResponseGeneratorImpl](-device-response-generator-impl/index.md) | [androidJvm]<br>class [DeviceResponseGeneratorImpl](-device-response-generator-impl/index.md)(val context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentsResolver: [DocumentsResolver](../eu.europa.ec.eudi.iso18013.transfer/-documents-resolver/index.md), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) : [ResponseGenerator](-response-generator/index.md)&lt;[DeviceRequest](-device-request/index.md)&gt; <br>DeviceResponseGeneratorImpl class is used for parsing a DeviceRequest and generating the DeviceResponse |
| [Request](-request/index.md) | [androidJvm]<br>interface [Request](-request/index.md) |
| [Response](-response/index.md) | [androidJvm]<br>interface [Response](-response/index.md) |
| [ResponseGenerator](-response-generator/index.md) | [androidJvm]<br>abstract class [ResponseGenerator](-response-generator/index.md)&lt;[T](-response-generator/index.md)&gt;<br>Response Generator |
| [SessionTranscriptBytes](index.md#-274759174%2FClasslikes%2F-360525760) | [androidJvm]<br>typealias [SessionTranscriptBytes](index.md#-274759174%2FClasslikes%2F-360525760) = [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
