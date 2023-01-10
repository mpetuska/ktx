package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.DirService
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import org.koin.test.inject

class CleanTest : KtxTest("clean") {
  private val dirService: DirService by inject()

  @Test
  fun cleanCache() {
    fileSystem.createDirectories(dirService.cache)
    val file = dirService.cache.resolve("someFile.txt").also { fileSystem.sink(it, true).close() }
    val dir = dirService.cache.resolve("someDir").also {
      fileSystem.createDirectories(it)
      fileSystem.sink(it.resolve("child.txt"), true).close()
    }
    execute("--cache")
    fileSystem.exists(dirService.cache).shouldBeTrue()
    fileSystem.exists(file).shouldBeFalse()
    fileSystem.exists(dir).shouldBeFalse()
  }
}
