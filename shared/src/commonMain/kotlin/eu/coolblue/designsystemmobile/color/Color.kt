package eu.coolblue.designsystemmobile.color

class Color(val value: Long) {
    val red: Int
        get() = ((value shr 16) and 0xFF).toInt()
    val green: Int
        get() = ((value shr 8) and 0xFF).toInt()
    val blue: Int
        get() = (value and 0xFF).toInt()
}
