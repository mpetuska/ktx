package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.SystemService
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.koin.test.inject
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class VersionTest : KtxTest("version") {
  private val systemService: SystemService by inject()

  @Test
  fun works() {
    val stdout = System.out
    try {
      val out = ByteArrayOutputStream().use {
        System.setOut(PrintStream(it))
        execute()
        it
      }
      String(out.toByteArray(), StandardCharsets.UTF_8) shouldContain systemService.version
    } finally {
      System.setOut(stdout)
    }
  }
}
