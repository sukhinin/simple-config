package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class FallbackConfigTest : ShouldSpec({
    should("return union of keys") {
        val config1 = SimpleConfig(mapOf("key1" to "", "key2" to ""))
        val config2 = SimpleConfig(mapOf("key2" to "", "key3" to ""))
        FallbackConfig(config1, config2).keys shouldBe setOf("key1", "key2", "key3")
    }

    should("return union of labels") {
        val config1 = SimpleConfig(mapOf("key11.key12" to "", "key21.keyXX" to ""))
        val config2 = SimpleConfig(mapOf("key21.keyYY" to "", "key31.key32" to ""))
        FallbackConfig(config1, config2).labels shouldBe setOf("key11", "key21", "key31")
    }

    should("be empty when both primary and fallback configs are empty") {
        val config1 = SimpleConfig(mapOf("key" to "value"))
        val config2 = SimpleConfig(emptyMap())
        FallbackConfig(config1, config1).isEmpty shouldBe false
        FallbackConfig(config1, config2).isEmpty shouldBe false
        FallbackConfig(config2, config1).isEmpty shouldBe false
        FallbackConfig(config2, config2).isEmpty shouldBe true
    }

    should("get value by key with fallback") {
        val config1 = SimpleConfig(mapOf("key1" to "value11"))
        val config2 = SimpleConfig(mapOf("key1" to "value12", "key2" to "value22"))
        FallbackConfig(config1, config2).get("key1") shouldBe "value11"
        FallbackConfig(config1, config2).get("key2") shouldBe "value22"
    }

    should("test for scoped key presence") {
        val config1 = SimpleConfig(mapOf("key1" to "value11"))
        val config2 = SimpleConfig(mapOf("key1" to "value12", "key2" to "value22"))
        FallbackConfig(config1, config2).contains("key1") shouldBe true
        FallbackConfig(config1, config2).contains("key2") shouldBe true
        FallbackConfig(config1, config2).contains("key3") shouldBe false
    }
})
