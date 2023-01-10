package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.DirService
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.file.shouldBeExecutable
import io.kotest.matchers.should
import okio.Path.Companion.toPath
import org.junit.jupiter.api.Test
import org.koin.test.inject

class InstallTest : KtxTest("install") {
  private val dirService: DirService by inject()

  @Test
  fun installScript() {
    execute(extractTempResource("/test-script.main.kts".toPath()).toString())
    dirService.scripts.resolve("test-script.main.kts").should {
      fileSystem.exists(it).shouldBeTrue()
      it.toFile().shouldBeExecutable()
    }
    dirService.bin.resolve("test-script").should {
      fileSystem.exists(it).shouldBeTrue()
      it.toFile().shouldBeExecutable()
    }
  }

  @Test
  fun installScriptWithAlias() {
    execute(extractTempResource("/test-script.main.kts".toPath()).toString(), "--alias", "custom-alias")
    dirService.scripts.resolve("test-script.main.kts").should {
      fileSystem.exists(it).shouldBeTrue()
      it.toFile().shouldBeExecutable()
    }
    dirService.bin.resolve("custom-alias").should {
      fileSystem.exists(it).shouldBeTrue()
      it.toFile().shouldBeExecutable()
    }
  }
}
