package dev.petuska.ktx.util

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import dev.petuska.ktx.domain.TargetKind

fun CliktCommand.targetKindOption() = option().switch(
  "--script" to TargetKind.SCRIPT,
  "--jar" to TargetKind.JAR,
).default(TargetKind.AUTO)
