package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.file
import dev.petuska.ktx.service.KtsService
import dev.petuska.ktx.service.SystemService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.valueOrThrow

@Single
class Execute : CliktCommand(
  help = "Executes a local script assuming all arguments to be script arguments",
  hidden = true,
  treatUnknownOptionsAsArgs = true,
), KoinComponent {
  init {
    context { helpOptionNames = setOf() }
  }

  private val target by argument(
    help = "Script file path to execute"
  ).file(mustExist = true, canBeDir = false)
  private val args by argument(
    help = "Arguments to be propagated to the script"
  ).multiple()

  private val systemService: SystemService by inject()
  private val ktsService: KtsService by inject()

  override fun run() = executeScript(target, args.toTypedArray())

  internal fun executeScript(target: File, args: Array<String>) = runBlocking {
    val result = ktsService.evalFile(target, args = args)
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
