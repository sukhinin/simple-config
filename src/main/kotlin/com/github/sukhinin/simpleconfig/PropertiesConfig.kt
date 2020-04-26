package com.github.sukhinin.simpleconfig

import java.util.*
import kotlin.NoSuchElementException

class PropertiesConfig(val properties: Properties) : Config {
    override val keys: Set<String> get() = properties.stringPropertyNames()
    override fun get(key: String): String = properties.getProperty(key) ?: throw NoSuchElementException(key)
}
