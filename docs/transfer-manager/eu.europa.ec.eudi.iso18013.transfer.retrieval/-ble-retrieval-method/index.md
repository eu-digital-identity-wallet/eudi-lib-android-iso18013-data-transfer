//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.retrieval](../index.md)/[BleRetrievalMethod](index.md)

# BleRetrievalMethod

[androidJvm]\
data class [BleRetrievalMethod](index.md)(val peripheralServerMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val centralClientMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val clearBleCache: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [DeviceRetrievalMethod](../../eu.europa.ec.eudi.iso18013.transfer/-device-retrieval-method/index.md)

BLE Retrieval Method

## Constructors

| | |
|---|---|
| [BleRetrievalMethod](-ble-retrieval-method.md) | [androidJvm]<br>constructor(peripheralServerMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), centralClientMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), clearBleCache: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name                                              | Summary                                                                                                                                                                                                   |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [centralClientMode](central-client-mode.md)       | [androidJvm]<br>val [centralClientMode](central-client-mode.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>set if the central client mode is enabled          |
| [clearBleCache](clear-ble-cache.md)               | [androidJvm]<br>val [clearBleCache](clear-ble-cache.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>set if the BLE cache should be cleared                     |
| [peripheralServerMode](peripheral-server-mode.md) | [androidJvm]<br>val [peripheralServerMode](peripheral-server-mode.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>set if the peripheral server mode is enabled |
