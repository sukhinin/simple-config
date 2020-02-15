package com.github.sukhinin.simpleconfig

class FallbackConfig(val config: Config, val fallback: Config) : Config {

    override val keys: Set<String> get() = config.keys.union(fallback.keys)

    override val labels: Set<String> get() = config.labels.union(fallback.labels)

    override val isEmpty: Boolean get() = config.isEmpty && fallback.isEmpty

    override fun get(key: String): String = if (config.contains(key)) config.get(key) else fallback.get(key)

    override fun contains(key: String): Boolean = config.contains(key) || fallback.contains(key)
}
