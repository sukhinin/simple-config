package com.github.sukhinin.simpleconfig

fun Config.withFallback(fallback: Config) = FallbackConfig(this, fallback)
