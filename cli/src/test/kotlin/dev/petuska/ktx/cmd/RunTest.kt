package dev.petuska.ktx.cmd

import dev.petuska.ktx.test.util.findResource
import org.junit.jupiter.api.Test

class RunTest : KtxTest("run") {
  @Test
  fun runScript() {
    execute(findResource("/test-script.main.kts").path)
  }
}
