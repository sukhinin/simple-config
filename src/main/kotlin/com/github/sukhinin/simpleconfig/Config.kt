package com.github.sukhinin.simpleconfig

import java.util.*

interface Config {

    val keys: Set<String>

    val labels: Set<String> get() = keys.asSequence().map { it.substringBefore('.') }.toSet()

    val size: Int get() = keys.size

    val isEmpty: Boolean get() = keys.isEmpty()

    fun contains(key: String) = keys.contains(key)

    fun get(key: String): String

    fun getOrDefault(key: String, default: String) = if (contains(key)) get(key) else default

    fun getBoolean(key: String) = get(key).toBoolean()

    fun getBooleanOrDefault(key: String, default: Boolean) = if (contains(key)) getBoolean(key) else default

    fun getInteger(key: String) = get(key).toInt()

    fun getIntegerOrDefault(key: String, default: Int) = if (contains(key)) getInteger(key) else default

    fun getLong(key: String) = get(key).toLong()

    fun getLongOrDefault(key: String, default: Long) = if (contains(key)) getLong(key) else default

    fun getDouble(key: String) = get(key).toDouble()

    fun getDoubleOrDefault(key: String, default: Double) = if (contains(key)) getDouble(key) else default

    fun getList(key: String) = get(key).split(',').map { it.trim() }.filter { it.isNotEmpty() }

    fun getListOrDefault(key: String, default: List<String>) = if (contains(key)) getList(key) else default

    fun toMap(): Map<String, String> = keys.associateWith { get(it) }

    fun toProperties(): Properties = Properties().also { it.putAll(toMap()) }

    fun withFallback(fallback: Config) = FallbackConfig(this, fallback)

    fun scoped(prefix: String) = ScopedConfig(this, prefix)

    fun scopedByLabel() = labels.associateWith { scoped(it) }

    fun resolved() = ResolvedConfig(this)

    fun masked() = MaskedConfig(this)

    fun masked(keywords: Collection<String>) = MaskedConfig(this, keywords)

    fun dump() = keys.sorted().joinToString("\n") { key -> "$key=${get(key)}" }

}
