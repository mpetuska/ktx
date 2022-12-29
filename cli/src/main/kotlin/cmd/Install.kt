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
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

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

  private suspend fun installScript(scriptFile: File) {
    val destination = fileService.copyScript(scriptFile, force)
    fileService.writeBin(
      name = alias ?: scriptFile.name.removeSuffix(".main.kts").removeSuffix(".kts"),
      content = ByteReadChannel(
        """
        #!/usr/bin/env bash
        ktx run --script ${destination.absolutePath} ${'$'}@
        """.trimIndent().toByteArray()
      ),
      force = force,
    )
  }
}
