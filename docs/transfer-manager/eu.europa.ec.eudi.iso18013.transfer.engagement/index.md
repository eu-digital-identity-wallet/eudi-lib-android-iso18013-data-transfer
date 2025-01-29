//[transfer-manager](../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.engagement](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [BleRetrievalMethod](-ble-retrieval-method/index.md) | [androidJvm]<br>data class [BleRetrievalMethod](-ble-retrieval-method/index.md)(val peripheralServerMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val centralClientMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val clearBleCache: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) : [DeviceRetrievalMethod](-device-retrieval-method/index.md)<br>BLE Retrieval Method |
| [DeviceRetrievalMethod](-device-retrieval-method/index.md) | [androidJvm]<br>interface [DeviceRetrievalMethod](-device-retrieval-method/index.md) : [RetrievalMethod](-retrieval-method/index.md)<br>Device Retrieval Method |
| [NfcEngagementService](-nfc-engagement-service/index.md) | [androidJvm]<br>abstract class [NfcEngagementService](-nfc-engagement-service/index.md) : [HostApduService](https://developer.android.com/reference/kotlin/android/nfc/cardemulation/HostApduService.html)<br>Abstract Nfc engagement service. |
| [QrCode](-qr-code/index.md) | [androidJvm]<br>data class [QrCode](-qr-code/index.md)(val content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br>Wrapper for a QR code. |
| [RetrievalMethod](-retrieval-method/index.md) | [androidJvm]<br>interface [RetrievalMethod](-retrieval-method/index.md)<br>Retrieval Method |
