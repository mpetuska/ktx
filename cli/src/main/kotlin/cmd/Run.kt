package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import dev.petuska.ktx.runner.ScriptRunner
import java.io.File
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.valueOrThrow
import kotlin.system.exitProcess

class Run : CliktCommand(
  help = "Download and execute a binary"
) {
  private val target by argument()

  @Suppress("NestedBlockDepth")
  override fun run() {
    if (target.endsWith(".main.kts")) {
      val res = ScriptRunner.evalFile(File(target).absoluteFile, "arg1")
      res.reports.forEach {
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

      val value = res.valueOrThrow().returnValue
      if (value is ResultValue.Error) {
        echo("ERROR: ${value.error.message}", err = true)
      }
      exitProcess(if (res is ResultWithDiagnostics.Failure || value is ResultValue.Error) 1 else 0)
    }
  }
}
