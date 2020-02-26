package com.github.sukhinin.simpleconfig

import com.github.sukhinin.simpleconfig.VariableResolver.Token
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec

internal class VariableResolverTest : ShouldSpec({

    should("tokenize single-token strings") {
        val resolver = VariableResolver(MapStringLookup(emptyMap()))
        resolver.tokenize("").isEmpty() shouldBe true
        resolver.tokenize("\$\${name}") shouldBe listOf(Token.Escape(0, 1))
        resolver.tokenize("\${name}") shouldBe listOf(Token.Variable(0, 7))
        resolver.tokenize("abc\$\${name}def") shouldBe listOf(Token.Escape(3, 4))
        resolver.tokenize("abc\${name}def") shouldBe listOf(Token.Variable(3, 10))
    }

    should("tokenize multiple-token strings") {
        val resolver = VariableResolver(MapStringLookup())
        resolver.tokenize("\${name}def\$\${x}ghi\${name}") shouldBe listOf(
            Token.Variable(0, 7),
            Token.Escape(10, 11),
            Token.Variable(18, 25)
        )
    }

    should("leave strings without placeholders unmodified") {
        val resolver = VariableResolver(MapStringLookup("KEY_A" to "VALUE_A", "KEY_B" to "VALUE_B"))
        resolver.resolve("") shouldBe ""
        resolver.resolve("abc def ghi") shouldBe "abc def ghi"
    }

    should("resolve simple placeholders") {
        val resolver = VariableResolver(MapStringLookup("KEY_A" to "VALUE_A", "KEY_B" to "VALUE_B"))
        resolver.resolve("abc \$\${KEY_A} def \$\${KEY_B} ghi") shouldBe "abc \${KEY_A} def \${KEY_B} ghi"
        resolver.resolve("abc \${KEY_A} def \${KEY_B} ghi") shouldBe "abc VALUE_A def VALUE_B ghi"
    }

    should("resolve nested placeholders") {
        val resolver = VariableResolver(MapStringLookup("KEY_A" to "B", "KEY_B" to "VALUE_B"))
        resolver.resolve("abc \${KEY_\${KEY_A}} def") shouldBe "abc VALUE_B def"
        resolver.resolve("abc \${UNKN:-DFLT_\${KEY_A}} def") shouldBe "abc DFLT_B def"
    }

    should("resolve recursive placeholders") {
        val resolver = VariableResolver(MapStringLookup("KEY_A" to "\${KEY_B}", "KEY_B" to "VALUE_B"))
        resolver.resolve("abc \${KEY_A} def") shouldBe "abc VALUE_B def"
    }

    should("return defaults when lookup value is not available") {
        val resolver = VariableResolver(MapStringLookup())
        resolver.resolve("abc \${UNKN:-DFLT} def") shouldBe "abc DFLT def"
        resolver.resolve("abc \${UNKN} def") shouldBe "abc \${UNKN} def"
    }

    should("throw on cyclic dependencies") {
        val resolver = VariableResolver(MapStringLookup("KEY_A" to "x\${KEY_B}x", "KEY_B" to "y\${KEY_A}y"))
        shouldThrow<IllegalStateException> { resolver.resolve("\${KEY_A}") }
    }
})
