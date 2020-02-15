package com.github.sukhinin.simpleconfig

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import java.util.*

internal class ScopedConfigTest : ShouldSpec({

    should("ignore trailing prefix dot") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1.").toMap() shouldBe config.scoped("p1").toMap()
    }

    should("allow multiple levels in prefix") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1.").toMap() shouldBe mapOf("key11.keyXX" to "v1", "key12.keyYY" to "v2")
        config.scoped("p1.key11").toMap() shouldBe mapOf("keyXX" to "v1")
        config.scoped("p1.key11.keyXX").toMap() shouldBe emptyMap<String, String>()
    }

    should("scope keys by prefix") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").keys shouldBe setOf("key11.keyXX", "key12.keyYY")
        config.scoped("X").keys shouldBe emptySet()
    }

    should("scope labels by prefix") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").labels shouldBe setOf("key11", "key12")
    }

    should("return scoped config size") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").size shouldBe 2
        config.scoped("X").size shouldBe 0
    }

    should("be empty when scope contains no keys") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").isEmpty shouldBe false
        config.scoped("X").isEmpty shouldBe true
    }

    should("return value by scoped key") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").get("key11.keyXX") shouldBe "v1"
        shouldThrow<NoSuchElementException> { config.scoped("p1").get("key21.keyZZ") }
    }

    should("test for scoped key presence") {
        val config = SimpleConfig(
            mapOf(
                "p1.key11.keyXX" to "v1",
                "p1.key12.keyYY" to "v2",
                "p2.key21.keyZZ" to "v3"
            )
        )
        config.scoped("p1").contains("key11.keyXX") shouldBe true
        config.scoped("p2").contains("key11.keyXX") shouldBe false
    }
})
