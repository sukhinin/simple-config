package com.github.sukhinin.simpleconfig

class MaskedConfig(val config: Config, val keywords: Collection<String> = DEFAULT_KEYWORDS) : Config {
    companion object {
        @JvmStatic
        val DEFAULT_KEYWORDS = listOf("apikey", "passwd", "password", "secret")
    }

    override val keys = config.keys

    override fun get(key: String): String = if (keywords.any { key.contains(it) }) "******" else config.get(key)
}
