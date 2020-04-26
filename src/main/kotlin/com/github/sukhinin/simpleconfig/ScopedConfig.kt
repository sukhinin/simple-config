package com.github.sukhinin.simpleconfig

class ScopedConfig(val config: Config, val prefix: String) : Config {

    private val prefixAndDot = if (prefix.endsWith('.')) prefix else "$prefix."

    override val keys: Set<String>
        get() = config.keys.asSequence().filter { it.startsWith(prefixAndDot) }
            .map { it.removePrefix(prefixAndDot) }.toSet()

    override fun get(key: String): String = config.get(prefixAndDot + key)

}

