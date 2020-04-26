package com.github.sukhinin.simpleconfig

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class MaskedConfigUtilsTest : ShouldSpec({

    should("create masked config with default keywords") {
        val originalConfig = MapConfig(emptyMap())
        val config = originalConfig.masked()
        config.config shouldBeSameInstanceAs originalConfig
        config.keywords shouldBe MaskedConfig.DEFAULT_KEYWORDS
    }

    should("create masked config with custom keywords") {
        val originalConfig = MapConfig(emptyMap())
        val keywords = listOf("secret")
        val config = originalConfig.masked(keywords)
        config.config shouldBeSameInstanceAs originalConfig
        config.keywords shouldBe keywords
    }

})
