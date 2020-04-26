package com.github.sukhinin.simpleconfig

class FallbackConfig(val config: Config, val fallback: Config) : Config {

    override val keys: Set<String> get() = config.keys.union(fallback.keys)

    override fun get(key: String): String = if (config.contains(key)) config.get(key) else fallback.get(key)

}
