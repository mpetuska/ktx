package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.DirService
import dev.petuska.ktx.test.util.findResource
import io.kotest.matchers.file.shouldBeExecutable
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.should
import org.junit.jupiter.api.Test
import org.koin.test.inject

class InstallTest : KtxTest("install") {
  private val dirService: DirService by inject()

  @Test
  fun installScript() {
    execute(findResource("/test-script.main.kts").path)
    dirService.scripts.resolve("test-script.main.kts").should {
      it.shouldExist()
      it.shouldBeExecutable()
    }
  }
}
