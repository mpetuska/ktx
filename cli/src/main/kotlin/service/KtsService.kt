package dev.petuska.ktx.service

import org.jetbrains.kotlin.mainKts.MainKtsEvaluationConfiguration
import org.jetbrains.kotlin.mainKts.MainKtsScript
import org.koin.core.annotation.Single
import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

@Single
class KtsService {
  private val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<MainKtsScript> {
    compilerOptions.put(listOf("-jvm-target", "11"))
  }

  fun evalFile(scriptFile: File, vararg args: String): ResultWithDiagnostics<EvaluationResult> {
    val source = scriptFile.toScriptSource()
    val evaluationConfiguration = MainKtsEvaluationConfiguration.with {
      constructorArgs(args)
    }
    return BasicJvmScriptingHost().eval(source, compilationConfiguration, evaluationConfiguration)
  }
}
