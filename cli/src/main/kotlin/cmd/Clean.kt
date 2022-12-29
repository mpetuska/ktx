package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.petuska.ktx.service.DirService
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@Single
class Clean : CliktCommand(
  help = "Clean ktx directories",
), KoinComponent {
  private val all by option().flag()
  private val bin by option().flag()
  private val cache by option().flag()
  private val scripts by option().flag()
  private val jars by option().flag()

  private val dirService: DirService by inject()

  override fun run() {
    if (cache) dirService.cache.clear()
    if (all) dirService.home.clear()
    if (bin) dirService.bin.clear()
    if (scripts) dirService.scripts.clear()
    if (jars) dirService.jars.clear()
  }

  private fun File.clear() = listFiles()?.forEach {
    echo("Removing ${it.absolutePath}")
    it.deleteRecursively()
  }
}
