package com.github.sukhinin.simpleconfig

class MaskedConfig(val config: Config, val keywords: Collection<String> = DEFAULT_KEYWORDS) : Config by config {
    companion object {
        @JvmStatic
        val DEFAULT_KEYWORDS = listOf("passwd", "password", "secret")
    }

    override fun get(key: String): String = if (keywords.any { key.contains(it) }) "******" else config.get(key)
}
