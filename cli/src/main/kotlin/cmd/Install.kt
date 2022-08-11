package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import dev.petuska.ktx.domain.SourceKind
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.runBlocking
import java.io.File

class Install : CliktCommand(
  help = "Download and install a script or binary"
) {
  private val configDir = File("${System.getenv("HOME")}/.ktx")
//  private val cacheDir = configDir.resolve("cache")
  private val scriptsDir = configDir.resolve("scripts")
  private val binDir = configDir.resolve("bin")

  private val source by argument()
  private val sourceKind by option().switch(
    "--script" to SourceKind.SCRIPT,
    "--jar" to SourceKind.JAR,
  ).default(SourceKind.AUTO)
  private val force by option().flag()
  private val alias by option()

  override fun run() {
    val remote = Regex("^http(s)?://.*").matches(source.lowercase())
    when (sourceKind) {
      SourceKind.AUTO,
      SourceKind.SCRIPT -> installScript(remote)

      SourceKind.JAR -> TODO()
    }
  }

  private fun installScript(remote: Boolean) {
    val scriptFile = File(source)
    val destination = scriptsDir.resolve(scriptFile.name).apply { parentFile.mkdirs() }
    if (remote) {
      runBlocking {
        HttpClient().get(source).bodyAsChannel().copyAndClose(destination.writeChannel())
      }
    } else {
      File(source).copyTo(destination, overwrite = force)
    }
    val binFile = binDir.resolve(alias ?: scriptFile.nameWithoutExtension).apply { parentFile.mkdirs() }
    if (force && binFile.exists()) binFile.delete()
    binFile.writeText(
      """
      #!/usr/bin/env bash
      ktx run ${destination.absolutePath} $@
      """.trimIndent()
    )
    binFile.setExecutable(true)
  }
}
