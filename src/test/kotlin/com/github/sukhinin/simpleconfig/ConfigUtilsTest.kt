package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.lang.NumberFormatException
import java.util.*

class ConfigUtilsTest : ShouldSpec({

    should("return label set from config keys") {
        val config = MapConfig(mapOf("11" to "", "21." to "", "21.22" to "", "31.32.33" to ""))
        config.labels() shouldBe setOf("11", "21", "31")
    }

    should("return config size") {
        val map = mapOf("1" to "", "2" to "")
        val config = MapConfig(map)
        config.size() shouldBe map.size
    }

    should("test if config is empty") {
        MapConfig(emptyMap()).isEmpty() shouldBe true
        MapConfig(mapOf("" to "")).isEmpty() shouldBe false
    }

    should("test if config contains a key") {
        val config = MapConfig(mapOf("k" to ""))
        config.contains("k") shouldBe true
        config.contains("x") shouldBe false
    }

    should("return string value with fallback") {
        val config = MapConfig(mapOf("k" to "v"))
        config.getOrDefault("k", "d") shouldBe "v"
        config.getOrDefault("x", "d") shouldBe "d"
    }

    should("convert to boolean value with fallback") {
        val config = MapConfig(mapOf("k1" to "true", "k2" to "none"))
        config.getBoolean("k1") shouldBe true
        config.getBoolean("k2") shouldBe false
        config.getBooleanOrDefault("x", true) shouldBe true
        config.getBooleanOrDefault("x", false) shouldBe false
    }

    should("convert to integer value with fallback") {
        val config = MapConfig(mapOf("k1" to Int.MAX_VALUE.toString(), "k2" to "none"))
        config.getInteger("k1") shouldBe Int.MAX_VALUE
        shouldThrow<NumberFormatException> { config.getInteger("k2") }
        config.getIntegerOrDefault("x", 42) shouldBe 42
    }

    should("convert to long value with fallback") {
        val config = MapConfig(mapOf("k1" to Long.MAX_VALUE.toString(), "k2" to "none"))
        config.getLong("k1") shouldBe Long.MAX_VALUE
        shouldThrow<NumberFormatException> { config.getLong("k2") }
        config.getLongOrDefault("x", 42L) shouldBe 42L
    }

    should("convert to double value with fallback") {
        val config = MapConfig(mapOf("k1" to Double.NaN.toString(), "k2" to "none"))
        config.getDouble("k1") shouldBe Double.NaN
        shouldThrow<NumberFormatException> { config.getDouble("k2") }
        config.getDoubleOrDefault("x", 42.0) shouldBe 42.0
    }

    should("convert to list with fallback") {
        val config = MapConfig(mapOf("k1" to "a", "k2" to "a, b ,c"))
        config.getList("k1") shouldBe listOf("a")
        config.getList("k2") shouldBe listOf("a", "b", "c")
        config.getListOrDefault("x", listOf("z")) shouldBe listOf("z")
    }

    should("convert to map") {
        val map = mapOf("k1" to "a", "k2" to "b")
        val config = MapConfig(map)
        config.toMap() shouldBe map
    }

    should("convert to properties") {
        val map = mapOf("k1" to "a", "k2" to "b")
        val config = MapConfig(map)
        config.toProperties() shouldBe Properties().apply { putAll(map) }
    }

    should("dump config to string") {
        val config = MapConfig(mapOf("k1" to "v1", "k2" to "v2"))
        config.dump() shouldBe "k1=v1\nk2=v2"
    }

})
