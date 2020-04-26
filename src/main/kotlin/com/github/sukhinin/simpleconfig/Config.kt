package com.github.sukhinin.simpleconfig

interface Config {
    val keys: Set<String>
    fun get(key: String): String
}
