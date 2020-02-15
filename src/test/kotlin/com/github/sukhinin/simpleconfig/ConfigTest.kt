package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class ConfigTest : ShouldSpec({

    should("extract labels from config keys") {
        val map = mapOf("key11" to "", "key21." to "", "key21.key22" to "", "key31.key32.key33" to "")
        val config = SimpleConfig(map)
        config.labels shouldBe setOf("key11", "key21", "key31")
    }

    should("convert config to map") {
        val map = mapOf("key1" to "value1", "key2" to "value2")
        val config = SimpleConfig(map)
        config.toMap() shouldBe map
    }

    should("convert config to properties") {
        val map = mapOf("key1" to "value1", "key2" to "value2")
        val config = SimpleConfig(map)
        config.toProperties() shouldBe Properties().apply { putAll(map) }
    }

    should("parse list values") {
        val config = SimpleConfig(
            mapOf(
                "key1" to "",
                "key2" to ",",
                "key3" to "value1, value2 ,"
            )
        )
        config.getList("key1") shouldBe emptyList()
        config.getList("key2") shouldBe emptyList()
        config.getList("key3") shouldBe listOf("value1", "value2")
    }
})
