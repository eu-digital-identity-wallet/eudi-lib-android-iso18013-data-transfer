//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../index.md)/[TransferManagerImpl](index.md)/[stopPresentation](stop-presentation.md)

# stopPresentation

[androidJvm]\
open override fun [stopPresentation](stop-presentation.md)(
sendSessionTerminationMessage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
useTransportSpecificSessionTermination: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

Closes the connection and clears the data of the session

Also, sends a termination message if there is a connected mdoc verifier

#### Parameters

androidJvm

|                                        |                                              |
|----------------------------------------|----------------------------------------------|
| sendSessionTerminationMessage          | Whether to send session termination message. |
| useTransportSpecificSessionTermination | Whether to use transport-specific session    |
