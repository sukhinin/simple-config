package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class PropertiesConfigTest : ShouldSpec({

    should("return keys from properties") {
        val properties = Properties().apply { putAll(mapOf("k1" to "v1", "k2" to "v2")) }
        val config = PropertiesConfig(properties)
        config.keys shouldBe properties.stringPropertyNames()
    }

    should("return values from properties") {
        val properties = Properties().apply { putAll(mapOf("k1" to "v1", "k2" to "v2")) }
        val config = PropertiesConfig(properties)
        properties.forEach { (k, v) -> config.get(k.toString()) shouldBe v }
        shouldThrow<NoSuchElementException> { config.get("x") }
    }

})
