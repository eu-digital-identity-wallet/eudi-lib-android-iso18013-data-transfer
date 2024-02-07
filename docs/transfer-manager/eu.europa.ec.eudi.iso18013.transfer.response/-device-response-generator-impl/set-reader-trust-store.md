//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[DeviceResponseGeneratorImpl](index.md)/[setReaderTrustStore](set-reader-trust-store.md)

# setReaderTrustStore

[androidJvm]\
open override fun [setReaderTrustStore](set-reader-trust-store.md)(readerTrustStore: [ReaderTrustStore](../../eu.europa.ec.eudi.iso18013.transfer.readerauth/-reader-trust-store/index.md)): [DeviceResponseGeneratorImpl](index.md)

Set a trust store so that reader authentication can be performed.

If it is not provided, reader authentication will not be performed.

#### Parameters

androidJvm

| | |
|---|---|
| readerTrustStore | a trust store for reader authentication, e.g. DefaultReaderTrustStore |
