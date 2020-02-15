package com.github.sukhinin.simpleconfig

class SimpleConfig(val map: Map<String, String> = emptyMap()) : Config {
    override val keys = map.keys
    override fun get(key: String) = map[key] ?: throw NoSuchElementException(key)
}
