package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class MaskedConfigTest : ShouldSpec({
    should("mask values by sensitive keywords") {
        val config = SimpleConfig(
            mapOf(
                "public" to "value1",
                "secret" to "value2",
                "topsecret" to "value3"
            )
        )
        val masked = config.masked(listOf("secret"))
        masked.get("public") shouldBe "value1"
        masked.get("secret") shouldBe "******"
        masked.get("topsecret") shouldBe "******"
    }
})
