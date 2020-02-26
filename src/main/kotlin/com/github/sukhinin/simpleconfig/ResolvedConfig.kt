package com.github.sukhinin.simpleconfig

class ResolvedConfig(val config: Config) : Config by config {

    private class Lookup(val config: Config) : StringLookup {
        override fun lookup(key: String): String? {
            return lookupSystemProperty(key)
                ?: lookupEnvironmentVariable(key)
                ?: lookupConfigKey(key)
        }

        private fun lookupSystemProperty(key: String): String? {
            return System.getProperty(key)
        }

        private fun lookupEnvironmentVariable(key: String): String? {
            return System.getenv(key)
        }

        private fun lookupConfigKey(key: String): String? {
            return if (config.contains(key)) config.get(key) else null
        }
    }

    private val lookup = Lookup(config)
    private val resolver = VariableResolver(lookup)

    override fun get(key: String): String {
        return resolver.resolve(config.get(key))
    }
}
