package dev.petuska.ktx.service

import org.jetbrains.kotlin.mainKts.MainKtsEvaluationConfiguration
import org.jetbrains.kotlin.mainKts.MainKtsScript
import org.koin.core.annotation.Single
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.api.with
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

@Single
class KtsService {
  private val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<MainKtsScript>()

  fun evalFile(scriptFile: File, vararg args: String): ResultWithDiagnostics<EvaluationResult> {
    val source = scriptFile.toScriptSource()
    val evaluationConfiguration = MainKtsEvaluationConfiguration.with { constructorArgs(args) }
    return BasicJvmScriptingHost().eval(source, compilationConfiguration, evaluationConfiguration)
  }
}
