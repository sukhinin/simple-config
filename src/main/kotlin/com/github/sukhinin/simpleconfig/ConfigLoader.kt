package com.github.sukhinin.simpleconfig

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

object ConfigLoader {

    @JvmStatic
    fun getConfigFromSystemProperties(prefix: String): Config {
        return System.getProperties().stringPropertyNames()
            .filter { name -> name.startsWith(prefix) }
            .associateWith { name -> System.getProperties().getProperty(name) }
            .mapKeys { (name, _) -> name.removePrefix(prefix) }
            .let(::MapConfig)
    }

    @JvmStatic
    fun getConfigFromPath(path: Path): Config {
        return Files.newInputStream(path, StandardOpenOption.READ)
            .use { stream -> Properties().apply { load(stream) } }
            .let { properties -> PropertiesConfig(properties) }
    }

    @JvmStatic
    fun getConfigFromSystemResource(name: String): Config {
        return ClassLoader.getSystemResourceAsStream(name)
            ?.use { stream -> Properties().apply { load(stream) } }
            ?.let { properties -> PropertiesConfig(properties) }
            ?: MapConfig(emptyMap())
    }
}
