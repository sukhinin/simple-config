package com.github.sukhinin.simpleconfig

class ScopedConfig(val config: Config, val prefix: String) : Config {

    private val prefixAndDot = if (prefix.endsWith('.')) prefix else "$prefix."

    override val keys: Set<String>
        get() = config.keys.asSequence().filter { it.startsWith(prefixAndDot) }
            .map { it.removePrefix(prefixAndDot) }.toSet()

    override val labels: Set<String>
        get() = config.keys.asSequence().filter { it.startsWith(prefixAndDot) }
            .map { it.removePrefix(prefixAndDot).substringBefore('.') }.toSet()

    override val size: Int get() = config.keys.count { it.startsWith(prefixAndDot) }

    override val isEmpty: Boolean get() = config.keys.none { it.startsWith(prefixAndDot) }

    override fun get(key: String): String = config.get(prefixAndDot + key)

    override fun contains(key: String): Boolean = config.contains(prefixAndDot + key)
}

