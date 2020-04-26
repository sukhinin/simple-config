package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class ScopedConfigTest : ShouldSpec({

    should("ignore trailing dot in prefix") {
        val config = MapConfig(mapOf("x.x" to "v1", "x.y" to "v2", "y.x" to "v3"))
        ScopedConfig(config, "x").toMap() shouldBe ScopedConfig(config, "x.").toMap()
    }

    should("allow multiple levels in prefix") {
        val config = MapConfig(mapOf("x.x.x" to "v1", "x.x.y" to "v2", "x.y.z" to "v3"))
        ScopedConfig(config, "x").toMap() shouldBe mapOf("x.x" to "v1", "x.y" to "v2", "y.z" to "v3")
        ScopedConfig(config, "x.x").toMap() shouldBe mapOf("x" to "v1", "y" to "v2")
        ScopedConfig(config, "x.x.x").toMap() shouldBe emptyMap<String, String>()
    }

})
