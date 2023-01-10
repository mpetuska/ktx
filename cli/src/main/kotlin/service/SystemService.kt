package dev.petuska.ktx.service

import org.koin.core.annotation.Single

interface SystemService {
  fun exitProcess(code: Int) {
    kotlin.system.exitProcess(code)
  }

  val version: String
}

@Single(binds = [SystemService::class])
class SystemServiceImpl : SystemService {
  override val version: String = this::class.java.getResource("/.version")?.readText()
    ?: error("Unable to determine self-version")
}
