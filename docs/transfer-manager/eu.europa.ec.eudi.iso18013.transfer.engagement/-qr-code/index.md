//[transfer-manager](../../../index.md)/[eu.europa.ec.eudi.iso18013.transfer.engagement](../index.md)/[QrCode](index.md)

# QrCode

[androidJvm]\
data class [QrCode](index.md)(val content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))

Wrapper for a QR code.

## Constructors

| | |
|---|---|
| [QrCode](-qr-code.md) | [androidJvm]<br>constructor(content: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br>Creates a QR code with the given content |

## Properties

| Name | Summary |
|---|---|
| [content](content.md) | [androidJvm]<br>val [content](content.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the content of the QR code |

## Functions

| Name | Summary |
|---|---|
| [asBitmap](as-bitmap.md) | [androidJvm]<br>fun [asBitmap](as-bitmap.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Bitmap](https://developer.android.com/reference/kotlin/android/graphics/Bitmap.html)<br>Returns the QR code as a [Bitmap](https://developer.android.com/reference/kotlin/android/graphics/Bitmap.html) with the given [size](as-bitmap.md). |
| [asView](as-view.md) | [androidJvm]<br>fun [asView](as-view.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [View](https://developer.android.com/reference/kotlin/android/view/View.html)<br>Returns the QR code as a [View](https://developer.android.com/reference/kotlin/android/view/View.html) with the given [size](as-view.md). |
