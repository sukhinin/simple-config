package com.github.sukhinin.simpleconfig

class MapConfig(val map: Map<String, String>) : Config {

    override val keys: Set<String> get() = map.keys

    override val size: Int get() = map.size

    override val isEmpty: Boolean get() = map.isEmpty()

    override fun get(key: String): String = map[key] ?: throw NoSuchElementException(key)

    override fun contains(key: String): Boolean = map.containsKey(key)
}
