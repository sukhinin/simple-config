package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class MapConfigTest : ShouldSpec({
    should("throw when key not found") {
        val config = MapConfig(mapOf("key1" to ""))
        shouldNotThrow<Throwable> { config.get("key1") }
        shouldThrow<NoSuchElementException> { config.get("nokey") }
    }
})
