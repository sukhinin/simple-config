package com.github.sukhinin.simpleconfig

class MapConfig(val map: Map<String, String>) : Config {
    override val keys: Set<String> get() = map.keys
    override fun get(key: String): String = map[key] ?: throw NoSuchElementException(key)
}
