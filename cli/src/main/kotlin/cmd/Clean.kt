package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.petuska.ktx.service.DirService
import okio.FileSystem
import okio.Path
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
class Clean : CliktCommand(
  help = "Clean ktx directories",
), KoinComponent {
  private val all by option(
    "--all",
    "-a",
    help = "Clean all ktx directories"
  ).flag()
  private val bin by option(
    "--bin",
    "-b",
    help = "Clean ktx binaries"
  ).flag()
  private val cache by option(
    "--cache",
    "-c",
    help = "Clean ktx cache"
  ).flag()
  private val scripts by option(
    "--scripts",
    "-s",
    help = "Clean ktx scripts"
  ).flag()

  private val dirService: DirService by inject()
  private val fileSystem: FileSystem by inject()

  override fun run() {
    if (cache) dirService.cache.clear()
    if (all) dirService.home.clear()
    if (bin) dirService.bin.clear()
    if (scripts) dirService.scripts.clear()
  }

  private fun Path.clear() = fileSystem.list(this).forEach {
    echo("Removing $it")
    fileSystem.deleteRecursively(it)
  }
}
