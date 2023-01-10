package dev.petuska.ktx.config

import io.ktor.client.*
import okio.FileSystem
import org.koin.dsl.module

val utilKoin = module {
  single { HttpClient() }
  single { FileSystem.SYSTEM }
}
