package com.github.sukhinin.simpleconfig

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

object ConfigLoader {

    @JvmStatic
    fun getConfigFromSystemProperties(prefix: String): Config {
        val prefixAndDot = if (prefix.endsWith('.')) prefix else "$prefix."
        return System.getProperties().stringPropertyNames()
            .filter { name -> name.startsWith(prefixAndDot) }
            .associateWith { name -> System.getProperties().getProperty(name) }
            .mapKeys { (name, _) -> name.removePrefix(prefixAndDot) }
            .let(::MapConfig)
    }

    @JvmStatic
    fun getConfigFromPath(path: String): Config {
        return getConfigFromPath(Paths.get(path))
    }

    @JvmStatic
    fun getConfigFromPath(path: Path): Config {
        return Files.newInputStream(path, StandardOpenOption.READ)
            .use { stream -> Properties().apply { load(stream) } }
            .let { properties -> PropertiesConfig(properties) }
    }

    @JvmStatic
    fun getConfigFromSystemResource(name: String): Config {
        return getConfigFromResource(name, ClassLoader.getSystemClassLoader())
    }

    @JvmStatic
    fun getConfigFromResource(name: String, classLoader: ClassLoader): Config {
        return classLoader.getResourceAsStream(name)
            ?.use { stream -> Properties().apply { load(stream) } }
            ?.let { properties -> PropertiesConfig(properties) }
            ?: MapConfig(emptyMap())
    }
}
