package eu.europa.ec.eudi.iso18013.transfer.internal

import com.android.identity.util.CborUtil
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
    private val expectedMethodCount: Int,
) {

    companion object {
        @Parameters(name = "{index}: peripheralServerMode={0}, centralClientMode={1}, clearBleCache={2}")
        @JvmStatic
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(false, true, false, 1),
                arrayOf(false, false, false, 0),
                arrayOf(true, false, false, 1),
                arrayOf(true, true, false, 1),
                arrayOf(false, true, true, 1),
                arrayOf(false, false, true, 0),
                arrayOf(true, false, true, 1),
                arrayOf(true, true, true, 1),
            )
        }
    }

    @Test
    fun testDeviceRetrievalMethodConnectionMethodsAndTransportOptions() {
        val bleRetrievalMethod =
            BleRetrievalMethod(peripheralServerMode, centralClientMode, clearBleCache)
        val connectionMethods = bleRetrievalMethod.connectionMethod
        Assert.assertEquals(expectedMethodCount, connectionMethods.size)

        val transportOptions = listOf(bleRetrievalMethod).transportOptions
        Assert.assertEquals(clearBleCache, transportOptions.bleClearCache)

        val connectionMethod = connectionMethods.firstOrNull()
        CborUtil.toDiagnostics(connectionMethod?.toDeviceEngagement() ?: byteArrayOf())
            .also { println("DeviceEngagement: $it") }
    }

}