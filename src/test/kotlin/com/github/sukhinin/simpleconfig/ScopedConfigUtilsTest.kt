package com.github.sukhinin.simpleconfig

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class ScopedConfigUtilsTest : ShouldSpec({

    should("create scoped config") {
        val originalConfig = MapConfig(emptyMap())
        val config = originalConfig.scoped("test_prefix")
        config.config shouldBeSameInstanceAs originalConfig
        config.prefix shouldBe "test_prefix"
    }

    should("create scoped by label config map") {
        val originalConfig = MapConfig(mapOf("k1.1" to "11", "k1.2" to "12", "k2.1" to "21"))
        val map = originalConfig.scopedByLabel()
        map.size shouldBe 2
        map.getValue("k1").toMap() shouldBe mapOf("1" to "11", "2" to "12")
        map.getValue("k2").toMap() shouldBe mapOf("1" to "21")
    }

})
