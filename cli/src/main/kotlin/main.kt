package dev.petuska.ktx

import dev.petuska.ktx.cmd.CmdModule
import dev.petuska.ktx.cmd.Ktx
import dev.petuska.ktx.config.util
import dev.petuska.ktx.service.ServicesModule
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.ksp.generated.module

fun main(args: Array<String>) {
  startKoin {
    modules(
      ServicesModule.module,
      CmdModule.module,
      util,
    )
    environmentProperties()
  }
  Ktx().main(args)
}
