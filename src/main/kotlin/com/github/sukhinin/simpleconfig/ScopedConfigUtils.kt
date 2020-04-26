package com.github.sukhinin.simpleconfig

fun Config.scoped(prefix: String) = ScopedConfig(this, prefix)

fun Config.scopedByLabel() = labels().associateWith { scoped(it) }
