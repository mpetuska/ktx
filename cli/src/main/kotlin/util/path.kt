package dev.petuska.ktx.util

private val urlRegex by lazy { Regex("^http(s)?://.*") }

fun String.isUrl(): Boolean = urlRegex.matches(lowercase())
