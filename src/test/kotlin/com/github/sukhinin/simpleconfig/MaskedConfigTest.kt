package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class MaskedConfigTest : ShouldSpec({
    should("mask values by sensitive keywords") {
        val map = mapOf("public" to "value1", "secret" to "value2", "topsecret" to "value3")
        val config = MapConfig(map)
        val masked = MaskedConfig(config, listOf("secret"))
        masked.get("public") shouldBe map.getValue("public")
        masked.get("secret") shouldBe MaskedConfig.MASKED_VALUE
        masked.get("topsecret") shouldBe MaskedConfig.MASKED_VALUE
    }
})
