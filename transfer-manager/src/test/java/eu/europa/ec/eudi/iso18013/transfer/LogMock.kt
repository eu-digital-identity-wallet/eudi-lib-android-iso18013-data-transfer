package eu.europa.ec.eudi.iso18013.transfer

import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

fun mockAndroidLog() = mockStatic(android.util.Log::class.java).apply {
    val printToConsoleAnswer = PrintToConsoleAnswer()

    // Mocking different Log methods
    Mockito.`when`(android.util.Log.d(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.d(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.i(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.i(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.w(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.w(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.e(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.e(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.v(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.v(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.wtf(anyString(), anyString()))
        .thenAnswer(printToConsoleAnswer)
    Mockito.`when`(android.util.Log.wtf(anyString(), anyString(), any()))
        .thenAnswer(printToConsoleAnswer)
}

class PrintToConsoleAnswer : Answer<Int> {
    override fun answer(invocation: InvocationOnMock): Int {
        val methodName = invocation.method.name
        val tag = invocation.getArgument<String>(0)
        val msg = invocation.getArgument<String>(1)
        val throwable = invocation.arguments.getOrNull(2) as? Throwable
        println("Mocked Log - Method: $methodName, TAG: $tag, Message: $msg, Throwable: $throwable")
        return 0 // Return value for Log methods
    }
}