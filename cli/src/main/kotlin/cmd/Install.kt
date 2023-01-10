package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
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
  private val target by argument()
  private val kind by option().switch(
    "--script" to TargetKind.SCRIPT,
    "--jar" to TargetKind.JAR,
  ).default(TargetKind.AUTO)
  private val force by option().flag()
  private val alias by option()

  private val fileSystem: FileSystem by inject()
  private val fileService: FileService by inject()
  private val resourceService: ResourceService by inject()

  override fun run() = runBlocking {
    val file = resourceService.resolve(target)
    val script = when (kind) {
      TargetKind.SCRIPT -> file
      TargetKind.JAR -> TODO("JAR execution not supported yet")
      TargetKind.AUTO -> if (target.endsWith(".kts")) file else TODO("JAR execution not supported yet")
    }
    installScript(script)
  }

  private fun installScript(scriptPath: Path) {
    val lines = fileSystem.read(scriptPath) {
      buildList {
        while (!exhausted()) {
          add(readUtf8LineStrict())
        }
      }
    }.toMutableList()
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
