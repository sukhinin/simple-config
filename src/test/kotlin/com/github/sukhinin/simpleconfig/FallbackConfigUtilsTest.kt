package com.github.sukhinin.simpleconfig

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.specs.ShouldSpec

class FallbackConfigUtilsTest : ShouldSpec({

    should("create fallback config") {
        val originalConfig = MapConfig(emptyMap())
        val fallbackConfig = MapConfig(emptyMap())
        val config = originalConfig.withFallback(fallbackConfig)
        config.config shouldBeSameInstanceAs originalConfig
        config.fallback shouldBeSameInstanceAs fallbackConfig
    }

})
