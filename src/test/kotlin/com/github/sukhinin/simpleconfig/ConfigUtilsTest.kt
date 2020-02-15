package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class ConfigUtilsTest : ShouldSpec({

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
