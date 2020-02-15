@file:JvmName("ConfigUtils")

package com.github.sukhinin.simpleconfig

fun Config.withFallback(fallback: Config): FallbackConfig {
    return FallbackConfig(this, fallback)
}

fun Config.scoped(prefix: String): ScopedConfig {
    return ScopedConfig(this, prefix)
}

fun Config.scopedByLabel(): Map<String, Config> {
    return labels.associateWith { scoped(it) }
}

fun Config.resolved(): ResolvedConfig {
    return ResolvedConfig(this)
}

fun Config.masked(): MaskedConfig {
    return MaskedConfig(this)
}

fun Config.masked(keywords: Collection<String>): MaskedConfig {
    return MaskedConfig(this, keywords)
}

fun Config.dump(): String {
    return keys.sorted().joinToString("\n") { key -> "$key=${get(key)}" }
}
