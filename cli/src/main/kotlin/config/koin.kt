package dev.petuska.ktx.config

import io.ktor.client.*
import org.koin.dsl.module

val util = module {
  single { HttpClient() }
}
