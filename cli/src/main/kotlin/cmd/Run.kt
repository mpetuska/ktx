package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import dev.petuska.ktx.domain.TargetKind
import dev.petuska.ktx.service.KtsService
import dev.petuska.ktx.service.ResourceService
import dev.petuska.ktx.service.SystemService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.script.experimental.api.*
import kotlin.system.exitProcess

@Single
class Run(
  private val systemService: SystemService,
) : CliktCommand(
  help = "Download and execute a script or binary",
), KoinComponent {
  private val target by argument()
  private val kind by option().switch(
    "--script" to TargetKind.SCRIPT,
    "--jar" to TargetKind.JAR,
  ).default(TargetKind.AUTO)
  private val args by argument().multiple()

  private val resourceService: ResourceService by inject()
  private val ktsService: KtsService by inject()

  override fun run() = runBlocking {
    val file = resourceService.resolve(target)
    val script = when (kind) {
      TargetKind.SCRIPT -> file
      TargetKind.JAR -> TODO("JAR execution not supported yet")
      TargetKind.AUTO -> if (target.endsWith(".kts")) file else TODO("JAR execution not supported yet")
    }
    val result = ktsService.evalFile(script, args = args.toTypedArray())
    processResult(result)
  }

  private fun processResult(result: ResultWithDiagnostics<EvaluationResult>) {
    result.reports.forEach {
      if (it.severity >= ScriptDiagnostic.Severity.WARNING) {
        echo(
          message = "${it.severity.name}: ${it.message}",
          err = it.severity > ScriptDiagnostic.Severity.WARNING
        )
        it.exception?.let { ex ->
          echo(
            message = "${it.severity.name}: ${ex.stackTraceToString()}",
            err = it.severity > ScriptDiagnostic.Severity.WARNING
          )
        }
      }
    }

    val value = result.valueOrThrow().returnValue
    if (value is ResultValue.Error) {
      echo("ERROR: ${value.error.message}", err = true)
    }
    systemService.exitProcess(if (result is ResultWithDiagnostics.Failure || value is ResultValue.Error) 1 else 0)
  }
}
