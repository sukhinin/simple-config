package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*
import kotlin.NoSuchElementException

internal class MapConfigTest : ShouldSpec({

    should("return keys from map") {
        val map = mapOf("k1" to "v1", "k2" to "v2")
        val config = MapConfig(map)
        config.keys shouldBe map.keys
    }

    should("return values from map") {
        val map = mapOf("k1" to "v1", "k2" to "v2")
        val config = MapConfig(map)
        map.forEach { (k, v) -> config.get(k) shouldBe v }
        shouldThrow<NoSuchElementException> { config.get("x") }
    }

})
