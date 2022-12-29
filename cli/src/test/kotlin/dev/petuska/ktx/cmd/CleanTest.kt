package dev.petuska.ktx.cmd

import dev.petuska.ktx.service.DirService
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import org.junit.jupiter.api.Test
import org.koin.test.inject
import java.io.File

class CleanTest : KtxTest("clean") {
  private val dirService: DirService by inject()

  @Test
  fun cleanCache() {
    dirService.cache.mkdirs()
    val file = dirService.cache.resolve("someFile").also(File::createNewFile)
    val dir = dirService.cache.resolve("someDir").also {
      it.mkdirs()
      it.resolve("child").createNewFile()
    }
    execute()
    dirService.cache.shouldExist()
    file.shouldNotExist()
    dir.shouldNotExist()
  }
}
