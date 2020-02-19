package com.github.sukhinin.simpleconfig

import org.apache.commons.text.StringSubstitutor
import org.apache.commons.text.lookup.StringLookup
import org.apache.commons.text.lookup.StringLookupFactory

class ResolvedConfig(val config: Config) : Config by config {

    private class Lookup(val config: Config) : StringLookup {
        override fun lookup(key: String): String? {
            return lookupSystemProperty(key) ?: lookupEnvironmentVariable(key) ?: lookupConfigKey(key)
        }

        private fun lookupSystemProperty(key: String): String? {
            return StringLookupFactory.INSTANCE.systemPropertyStringLookup().lookup(key)
        }

        private fun lookupEnvironmentVariable(key: String): String? {
            return StringLookupFactory.INSTANCE.environmentVariableStringLookup().lookup(key)
        }

        private fun lookupConfigKey(key: String): String? {
            return if (config.contains(key)) config.get(key) else null
        }
    }

    private val lookup = Lookup(config)
    private val substitutor = StringSubstitutor(lookup)

    init {
        substitutor.isEnableSubstitutionInVariables = true
    }

    override fun get(key: String): String {
        return substitutor.replace(config.get(key))
    }
}
