package dev.petuska.ktx

import dev.petuska.ktx.cmd.CmdModule
import dev.petuska.ktx.cmd.Ktx
import dev.petuska.ktx.cmd.Migrate
import dev.petuska.ktx.config.utilKoin
import dev.petuska.ktx.service.ServicesModule
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.ksp.generated.module

fun main(vararg args: String) {
  val koin = startKoin {
    modules(
      ServicesModule.module,
      CmdModule.module,
      utilKoin,
    )
    environmentProperties()
  }.koin
  koin.get<Migrate>().main(arrayOf("--quiet", "--light"))
  Ktx().main(args)
}
