//[transfer-manager](../../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer](../../index.md)/[TransferManagerImpl](../index.md)/[Builder](index.md)/[zkResponsePolicy](zk-response-policy.md)

# zkResponsePolicy

[androidJvm]\
fun [zkResponsePolicy](zk-response-policy.md)(zkResponsePolicy: [ZkResponsePolicy](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-zk-response-policy/index.md)): &lt;Error class: unknown class&gt;

ZK response policy that determines behavior when ZK proof generation fails. Defaults to [ZkResponsePolicy.FallbackToFullDisclosure](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-zk-response-policy/-fallback-to-full-disclosure/index.md) for backwards compatibility. Consider using [ZkResponsePolicy.Strict](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-zk-response-policy/-strict/index.md) for production to prevent unintended full disclosure.

#### Parameters

androidJvm

| |
|---|
| zkResponsePolicy |

[androidJvm]\
var [zkResponsePolicy](zk-response-policy.md): [ZkResponsePolicy](../../../eu.europa.ec.eudi.iso18013.transfer.zkp/-zk-response-policy/index.md)
