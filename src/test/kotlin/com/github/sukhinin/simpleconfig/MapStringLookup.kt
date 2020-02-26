package com.github.sukhinin.simpleconfig

class MapStringLookup(private val map: Map<String, String>) : StringLookup {
    constructor(vararg pairs: Pair<String, String>) : this(mapOf(*pairs))
    override fun lookup(key: String): String? = map[key]
}
