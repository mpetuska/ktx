package dev.petuska.ktx.cmd

import okio.Path.Companion.toPath
import org.junit.jupiter.api.Test

class RunTest : KtxTest("run") {
  @Test
  fun runScript() {
    execute(extractTempResource("/test-script.main.kts".toPath()).toString())
  }
}
