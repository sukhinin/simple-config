package com.github.sukhinin.simpleconfig

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

object ConfigLoader {

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
