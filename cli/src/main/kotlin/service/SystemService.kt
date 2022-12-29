package dev.petuska.ktx.service

import org.koin.core.annotation.Single

interface SystemService {
  fun exitProcess(code: Int) {
    kotlin.system.exitProcess(code)
  }
}

@Single(binds = [SystemService::class])
class SystemServiceImpl : SystemService
