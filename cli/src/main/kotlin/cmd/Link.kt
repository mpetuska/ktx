package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.petuska.ktx.service.FileService
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@Single
class Link : CliktCommand(
  help = "Link a local script to be executable from PATH",
), KoinComponent {
  private val force by option(
    "--force",
    "-f",
    help = "Override existing installations if any exist",
  ).flag()
  private val alias by option(
    "--alias",
    "-a",
    help = "Alias to be set up to access script from the terminal",
  )
  private val target by argument(
    help = "File path to link",
  )

  private val fileService: FileService by inject()

  override fun run(): Unit = runBlocking {
    val file = File(target)
    require(file.exists())
    val script = """
      #!/usr/bin/env sh
      ktx execute ${file.absolutePath} $@
    """.trimIndent()

    val path = fileService.writeBin(
      alias ?: file.name.removeSuffix(".main.kts").removeSuffix(".kts"),
      Buffer().writeUtf8(script),
      force
    )
    path.toFile().setExecutable(true)
  }
}
