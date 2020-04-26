package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class FallbackConfigTest : ShouldSpec({

    should("return union of keys") {
        val config1 = MapConfig(mapOf("k1" to "", "k2" to ""))
        val config2 = MapConfig(mapOf("k2" to "", "k3" to ""))
        FallbackConfig(config1, config2).keys shouldBe setOf("k1", "k2", "k3")
    }

    should("get value by key with fallback") {
        val config1 = MapConfig(mapOf("k1" to "11"))
        val config2 = MapConfig(mapOf("k1" to "12", "k2" to "22"))
        FallbackConfig(config1, config2).get("k1") shouldBe "11"
        FallbackConfig(config1, config2).get("k2") shouldBe "22"
    }

})
