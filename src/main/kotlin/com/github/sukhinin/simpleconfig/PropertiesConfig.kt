package com.github.sukhinin.simpleconfig

import java.util.*
import kotlin.NoSuchElementException

class PropertiesConfig(val properties: Properties) : Config {

    override val keys: Set<String> get() = properties.stringPropertyNames()

    override val size: Int get() = properties.size

    override val isEmpty: Boolean get() = properties.isEmpty

    override fun get(key: String): String = properties.getProperty(key) ?: throw NoSuchElementException(key)

    override fun contains(key: String): Boolean = properties.containsKey(key)
}
