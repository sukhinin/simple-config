package com.github.sukhinin.simpleconfig

fun Config.masked() = MaskedConfig(this)

fun Config.masked(keywords: Collection<String>) = MaskedConfig(this, keywords)
