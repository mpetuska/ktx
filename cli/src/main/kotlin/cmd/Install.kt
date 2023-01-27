package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import dev.petuska.ktx.domain.TargetKind
import dev.petuska.ktx.service.FileService
import dev.petuska.ktx.service.ResourceService
import kotlinx.coroutines.runBlocking
import okio.Buffer
import okio.FileSystem
import okio.Path
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
class Install : CliktCommand(
  help = "Download and install a script or binary",
), KoinComponent {
  private val kind by option(
    help = "Installation target kind. Detected automatically if not specified"
  ).switch(
    "--script" to TargetKind.SCRIPT,
    "--jar" to TargetKind.PACKAGE,
  )
  private val force by option(
    "--force",
    "-f",
    help = "Override existing installations if any exist"
  ).flag()
  private val alias by option(
    "--alias",
    "-a",
    help = "Alias to be set up to access script from the terminal"
  )
  private val target by argument(
    help = "Script url, file path or gradle coordinate to install"
  )

  private val fileSystem: FileSystem by inject()
  private val fileService: FileService by inject()
  private val resourceService: ResourceService by inject()

  override fun run() = runBlocking {
    val script = resourceService.resolve(target, kind)
    installScript(script)
  }

  private fun installScript(scriptPath: Path) {
    val lines = fileSystem.read(scriptPath) {
      readUtf8()
    }.split("\n").toMutableList()
    if (lines.first().startsWith("#!")) {
      lines.removeFirst()
    }
    lines.add(0, "#!/usr/bin/env -S ktx execute")
    val script = fileService.writeScript(
      name = scriptPath.name,
      force = true,
      content = Buffer().apply { lines.forEach { writeUtf8(it + '\n') } }
    )
    script.toFile().setExecutable(true)
    fileService.linkBin(
      alias ?: scriptPath.name.removeSuffix(".main.kts").removeSuffix(".kts"),
      script,
      force
    )
  }
}
