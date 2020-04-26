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

    should("scope configs by label to map") {
        val config = SimpleConfig(
            mapOf(
                "key1.key11" to "value1",
                "key1.key12" to "value2",
                "key2.key21" to "value3"
            )
        )
        val labelToScopedConfigMap = config.scopedByLabel()
        labelToScopedConfigMap.getValue("key1").toMap() shouldBe mapOf("key11" to "value1", "key12" to "value2")
        labelToScopedConfigMap.getValue("key2").toMap() shouldBe mapOf("key21" to "value3")
    }

    should("dump config to string") {
        val config = SimpleConfig(
            mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )
        )
        config.dump() shouldBe "key1=value1\nkey2=value2"
    }

})
