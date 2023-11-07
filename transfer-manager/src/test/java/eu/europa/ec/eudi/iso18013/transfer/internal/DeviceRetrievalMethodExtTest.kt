package eu.europa.ec.eudi.iso18013.transfer.internal

import eu.europa.ec.eudi.iso18013.transfer.retrieval.BleRetrievalMethod
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class DeviceRetrievalMethodExtTest(
    private val peripheralServerMode: Boolean,
    private val centralClientMode: Boolean,
    private val clearBleCache: Boolean,
) {

    companion object {
        @Parameters(name = "{index}: peripheralServerMode={0}, centralClientMode={1}, clearBleCache={2}")
        @JvmStatic
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(false, true, false),
                arrayOf(false, false, false),
                arrayOf(true, false, false),
                arrayOf(true, true, false),
                arrayOf(false, true, true),
                arrayOf(false, false, true),
                arrayOf(true, false, true),
                arrayOf(true, true, true),
            )
        }
    }

    @Test
    fun testDeviceRetrievalMethodConnectionMethodsAndTransportOptions() {
        val bleRetrievalMethod =
            BleRetrievalMethod(peripheralServerMode, centralClientMode, clearBleCache)
        val connectionMethod = bleRetrievalMethod.connectionMethod
        Assert.assertEquals(1, connectionMethod.size)
        val transportOptions = listOf(bleRetrievalMethod).transportOptions
        Assert.assertEquals(clearBleCache, transportOptions.bleClearCache)
    }

}