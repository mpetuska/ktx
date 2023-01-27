package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import dev.petuska.ktx.domain.TargetKind
import dev.petuska.ktx.service.ResourceService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
class Run : CliktCommand(
  help = "Download and execute a script or binary",
  treatUnknownOptionsAsArgs = true,
), KoinComponent {
  private val kind by option(
    help = "Run target kind. Detected automatically if not specified"
  ).switch(
    "--script" to TargetKind.SCRIPT,
    "--package" to TargetKind.PACKAGE,
  )
  private val target by argument(
    help = "Script url, file path or gradle coordinate to run"
  )
  private val args by argument().multiple()

  private val resourceService: ResourceService by inject()
  private val executeCmd: Execute by inject()

  override fun run() = runBlocking {
    val script = resourceService.resolve(target, kind)
    executeCmd.executeScript(script.toFile(), args.toTypedArray())
  }
}
