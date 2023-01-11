package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.petuska.ktx.service.DirService
import dev.petuska.ktx.service.SystemService
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@Single
class Migrate(
  @Property("KTX_DIR") ktxDir: String?,
) : CliktCommand(
  help = "Migrate from previous ktx versions",
), KoinComponent {
  private val quiet by option().flag()
  private val systemService: SystemService by inject()
  private val dirService: DirService by inject()
  private val fileSystem: FileSystem by inject()

  private val currentVersionFile = ktxDir?.toPath()?.resolve(".version")
  private val currentVersion = currentVersionFile?.takeIf(fileSystem::exists)
    ?.let { fileSystem.read(it, BufferedSource::readUtf8) }
  private val selfVersion = systemService.version
  private val selfLocation by lazy {
    this::class.java.protectionDomain.codeSource.location.toURI().let(::File).parentFile
  }

  override fun run() {
    if (currentVersion != selfVersion) {
      if (!quiet) echo("New ktx version detected. Performing migrations...")
      switchRc()
      val start = currentVersion?.let { migrations.indexOfFirst { (v, _) -> v == currentVersion }.inc() } ?: 0
      var last: String? = null
      for (i in start until migrations.size) {
        val next = migrations[i]
        if (!quiet) echo("Migrating ktx${last?.let { " from $it" } ?: ""} to ${next.first}")
        next.second()
        last = next.first
      }
      currentVersionFile?.let(fileSystem::createDirectories)
      currentVersionFile?.let { fileSystem.write(it) { writeUtf8(selfVersion) } }
      if (!quiet) echo("ktx was migrated to $selfVersion successfully. Terminating current process...")
      systemService.exitProcess(0)
    } else {
      if (!quiet) echo("ktx is already migrated to $selfVersion version")
    }
  }

  /**
   * Migrations list that gets applied from [currentVersion] (excluded). The order is IMPORTANT!
   * @param key target version
   * @param value lambda that migrates the state to target version from previous migration
   */
  private val migrations: List<Pair<String, () -> Unit>> = listOf("0.0.2" to {
    dirService.home.resolve("jars").takeIf(fileSystem::exists)?.let(fileSystem::deleteRecursively)
    dirService.bin.takeIf(fileSystem::exists)?.let(fileSystem::list)?.forEach {
      echo("Removing $it due to incompatibility with ktx@$selfVersion. Please reinstall it.")
      fileSystem.deleteRecursively(it)
    }
  })

  private fun switchRc() {
    val ktxrc = selfLocation.parentFile.resolve(".ktxrc").absolutePath
    val sourceRegex = Regex("source \".*/\\.ktxrc\"")
    val sourceStr = "source \"$ktxrc\""
    val appendRc = { rcFile: Path ->
      var content = fileSystem.read(rcFile) { readUtf8() }
      if (content.contains(sourceRegex)) {
        content = content.replace(sourceRegex, sourceStr)
      } else {
        content += "$sourceStr\n"
      }
      fileSystem.write(rcFile) {
        writeUtf8(content)
      }
    }
    dirService.profile?.let { appendRc(it) }
    dirService.bashrc?.let { appendRc(it) }
    dirService.zshrc?.let { appendRc(it) }
  }
}
