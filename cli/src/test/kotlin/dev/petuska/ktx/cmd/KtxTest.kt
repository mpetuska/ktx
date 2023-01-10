package dev.petuska.ktx.cmd

import dev.petuska.ktx.config.utilKoin
import dev.petuska.ktx.service.ServicesModule
import dev.petuska.ktx.service.SystemService
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.junit5.KoinTestExtension
import test.util.ITest
import java.io.File

abstract class KtxTest(private val command: String) : ITest() {
  @field:TempDir
  protected lateinit var ktxHome: File

  @RegisterExtension
  override val koinExtension = KoinTestExtension.create {
    allowOverride(true)
    properties(mapOf("KTX_HOME" to ktxHome.absolutePath))
    modules(
      ServicesModule.module,
      CmdModule.module,
      utilKoin,
      module {
        single<SystemService> {
          object : SystemService {
            override fun exitProcess(code: Int) = Unit
            override val version: String = "0.0.0"
          }
        }
      }
    )
  }

  protected fun execute(vararg args: String) {
    Ktx().main(arrayOf(command) + args)
  }
}
