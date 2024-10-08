//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManagerImpl](index.md)/[startEngagementToApp](start-engagement-to-app.md)

# startEngagementToApp

[androidJvm]\
open override fun [startEngagementToApp](start-engagement-to-app.md)(
intent: [Intent](https://developer.android.com/reference/kotlin/android/content/Intent.html))

Starts the engagement to app, according to ISO 18013-7.

#### Parameters

androidJvm

|        |                                                                                                                                                                                                                                                                                           |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| intent | The intent being received If the transfer has already started, an error event will be triggered with an [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) containing the message &quot;Transfer has already started.&quot; |

#### See also

|                                                           |
|-----------------------------------------------------------|
| [TransferEvent.Error](../-transfer-event/-error/index.md) |
