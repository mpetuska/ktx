package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.ServicesModule
import dev.petuska.ktx.service.SystemService
import io.ktor.client.*
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import java.io.File

abstract class KtxTest(private val command: String) : KoinTest {
  @field:TempDir
  protected lateinit var ktxHome: File

  @RegisterExtension
  protected val koinExtension = KoinTestExtension.create {
    properties(mapOf("KTX_HOME" to ktxHome.absolutePath))
    modules(
      ServicesModule.module,
      CmdModule.module,
      module {
        single { HttpClient() }
        single<SystemService> {
          object : SystemService {
            override fun exitProcess(code: Int) = Unit
          }
        }
      }
    )
  }

  protected fun execute(vararg args: String) {
    Ktx().main(arrayOf(command) + args)
  }
}
