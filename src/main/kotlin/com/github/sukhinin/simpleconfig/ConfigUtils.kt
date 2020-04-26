package com.github.sukhinin.simpleconfig

import java.util.*

fun Config.labels() = keys.asSequence().map { it.substringBefore('.') }.toSet()

fun Config.size() = keys.size

fun Config.isEmpty() = keys.isEmpty()

fun Config.contains(key: String) = keys.contains(key)

fun Config.getOrDefault(key: String, default: String) = if (contains(key)) get(key) else default

fun Config.getBoolean(key: String) = get(key).toBoolean()

fun Config.getBooleanOrDefault(key: String, default: Boolean) = if (contains(key)) getBoolean(key) else default

fun Config.getInteger(key: String) = get(key).toInt()

fun Config.getIntegerOrDefault(key: String, default: Int) = if (contains(key)) getInteger(key) else default

fun Config.getLong(key: String) = get(key).toLong()

fun Config.getLongOrDefault(key: String, default: Long) = if (contains(key)) getLong(key) else default

fun Config.getDouble(key: String) = get(key).toDouble()

fun Config.getDoubleOrDefault(key: String, default: Double) = if (contains(key)) getDouble(key) else default

fun Config.getList(key: String) = get(key).split(',').map { it.trim() }.filter { it.isNotEmpty() }

fun Config.getListOrDefault(key: String, default: List<String>) = if (contains(key)) getList(key) else default

fun Config.toMap(): Map<String, String> = keys.associateWith { get(it) }

fun Config.toProperties(): Properties = Properties().also { it.putAll(toMap()) }

fun Config.dump() = keys.sorted().joinToString("\n") { key -> "$key=${get(key)}" }
