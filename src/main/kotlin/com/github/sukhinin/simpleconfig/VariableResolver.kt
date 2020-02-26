package com.github.sukhinin.simpleconfig

import java.util.*
import kotlin.collections.ArrayList

class VariableResolver(private val lookup: StringLookup) {
    private companion object {
        const val VARIABLE_PREFIX = "\${"
        const val VARIABLE_SUFFIX = "}"
        const val VARIABLE_ESCAPE = "$"
        const val DEFAULT_DELIMITER = ":-"
        const val ESCAPED_VARIABLE_PREFIX = VARIABLE_ESCAPE + VARIABLE_PREFIX

        val escapedPrefixOrPrefix = listOf(ESCAPED_VARIABLE_PREFIX, VARIABLE_PREFIX)
        val escapedPrefixOrPrefixOrSuffix = listOf(ESCAPED_VARIABLE_PREFIX, VARIABLE_PREFIX, VARIABLE_SUFFIX)
    }

    fun resolve(str: String): String {
        return resolve(str, Stack())
    }

    private fun resolve(str: String, vars: Stack<String>): String {
        val tokens = tokenize(str)
        return processTokens(str, tokens, vars)
    }

    private fun processTokens(str: String, tokens: List<Token>, vars: Stack<String>): String {
        val buf = StringBuilder(str)
        for (token in tokens.asReversed()) {
            when (token) {
                is Token.Escape -> processEscapeToken(buf, token)
                is Token.Variable -> processVariableToken(buf, token, vars)
            }
        }
        return buf.toString()
    }

    private fun processEscapeToken(buf: StringBuilder, token: Token.Escape) {
        buf.delete(token.start, token.end)
    }

    private fun processVariableToken(buf: StringBuilder, token: Token.Variable, vars: Stack<String>) {
        val nameAndDefault = buf.substring(token.start + VARIABLE_PREFIX.length, token.end - VARIABLE_SUFFIX.length)
        val resolvedNameAndDefault = resolve(nameAndDefault, vars)

        val index = resolvedNameAndDefault.indexOf(DEFAULT_DELIMITER)
        val name = if (index >= 0) resolvedNameAndDefault.substring(0, index) else resolvedNameAndDefault
        val default = if (index >= 0) resolvedNameAndDefault.substring(index + DEFAULT_DELIMITER.length) else null

        if (vars.contains(name)) {
            throw IllegalStateException("Detected variable resolution loop: ${vars.joinToString()}")
        }

        vars.push(name)
        val value = lookup.lookup(name)
        val resolvedValue = when {
            value != null -> resolve(value, vars)
            default != null -> resolve(default, vars)
            else -> "$VARIABLE_PREFIX$resolvedNameAndDefault$VARIABLE_SUFFIX"
        }
        vars.pop()

        buf.replace(token.start, token.end, resolvedValue)
    }

    internal fun tokenize(str: String): List<Token> {
        val tokens = ArrayList<Token>()
        var position = 0

        while (true) {
            // Looking for either escaped or unescaped prefix starting from the last match
            val m1 = str.findAnyOf(escapedPrefixOrPrefix, position) ?: break

            // It's an escaped variable prefix
            if (m1.second == ESCAPED_VARIABLE_PREFIX) {
                position = m1.first + ESCAPED_VARIABLE_PREFIX.length
                tokens.add(Token.Escape(m1.first, m1.first + VARIABLE_ESCAPE.length))
                continue
            }

            // It's an unescaped variable prefix, i.e. start of variable token
            position = m1.first + VARIABLE_PREFIX.length
            var nestedCount = 0
            while (true) {
                // Looking for escaped or unescaped prefix or suffix
                val m2 = str.findAnyOf(escapedPrefixOrPrefixOrSuffix, position) ?: break

                // Ignore escaped variable prefix, we'll handle it on next iteration
                if (m2.second == ESCAPED_VARIABLE_PREFIX) {
                    position = m2.first + ESCAPED_VARIABLE_PREFIX.length
                    continue
                }

                // Increase nested variable count on unescaped prefix
                if (m2.second == VARIABLE_PREFIX) {
                    position = m2.first + VARIABLE_PREFIX.length
                    nestedCount++
                    continue
                }

                // Here the only option left is a variable suffix
                position = m2.first + VARIABLE_SUFFIX.length
                if (nestedCount == 0) {
                    tokens.add(Token.Variable(m1.first, position))
                    break
                }
                nestedCount--
            }
        }

        return tokens
    }

    internal abstract class Token {
        data class Escape(val start: Int, val end: Int) : Token()
        data class Variable(val start: Int, val end: Int) : Token()
    }
}
