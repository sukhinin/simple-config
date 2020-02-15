package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

internal class ResolvedConfigTest : ShouldSpec({
    should("resolve config keys") {
        val config = SimpleConfig(
            mapOf(
                "key" to "a \${another.key} z",
                "another.key" to "value"
            )
        )
        config.resolved().get("key") shouldBe "a value z"
    }

    should("resolve environment variables") {
        assert(System.getenv().containsKey("PATH")) { "This test requires PATH environment variable to be set" }
        val config = SimpleConfig(mapOf("key" to "a \${PATH} z"))
        config.resolved().get("key") shouldBe "a ${System.getenv("PATH")} z"
    }

    should("resolve system properties") {
        System.setProperty("my.system.property", "my_system_property_value")
        val config =
            SimpleConfig(mapOf("key" to "a \${my.system.property} z"))
        config.resolved().get("key") shouldBe "a my_system_property_value z"
    }

    should("leave unknown placeholders unresolved") {
        val config =
            SimpleConfig(mapOf("key" to "a \${RANDOM_PLACEHOLDER_NHZS2K8HZT} z"))
        config.resolved().get("key") shouldBe "a \${RANDOM_PLACEHOLDER_NHZS2K8HZT} z"
    }
})
