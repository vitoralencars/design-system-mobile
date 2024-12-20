package eu.coolblue.designsystemmobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform