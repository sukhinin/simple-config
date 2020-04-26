package com.github.sukhinin.simpleconfig

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.specs.ShouldSpec

class ResolvedConfigUtilsTest : ShouldSpec({

    should("create resolved config") {
        val originalConfig = MapConfig(emptyMap())
        val config = originalConfig.resolved()
        config.config shouldBeSameInstanceAs originalConfig
    }

})
