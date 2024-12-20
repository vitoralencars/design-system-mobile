package eu.coolblue.designsystemmobile

import eu.coolblue.designsystemmobile.color.Color
import platform.UIKit.UIColor

fun Color.toUIColor() = UIColor(
    red = red.toDouble() / 255.0,
    green = green.toDouble() / 255.0,
    blue = blue.toDouble() / 255.0,
    alpha = 1.0
)
