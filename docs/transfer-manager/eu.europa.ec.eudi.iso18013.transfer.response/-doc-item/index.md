//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.response](../index.md)/[DocItem](index.md)

# DocItem

[androidJvm]\
data class [DocItem](index.md)(val
namespace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
elementIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Doc item represents a data element

## Constructors

|                         |                                                                                                                                                                                                                               |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DocItem](-doc-item.md) | [androidJvm]<br>constructor(namespace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), elementIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name                                       | Summary                                                                                                                                                                                                      |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [elementIdentifier](element-identifier.md) | [androidJvm]<br>val [elementIdentifier](element-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the data element identifier e.g. family_name, given_name |
| [namespace](namespace.md)                  | [androidJvm]<br>val [namespace](namespace.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the namespace that the element belong to, e.g. eu.europa.ec.eudiw.pid.1   |
