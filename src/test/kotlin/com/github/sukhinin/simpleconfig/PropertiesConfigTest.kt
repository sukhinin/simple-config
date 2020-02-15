package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class PropertiesConfigTest : ShouldSpec({
    should("throw when key not found") {
        val properties = Properties().apply { putAll(mapOf("key1" to "")) }
        val config = PropertiesConfig(properties)
        shouldNotThrow<Throwable> { config.get("key1") }
        shouldThrow<NoSuchElementException> { config.get("nokey") }
    }
})
