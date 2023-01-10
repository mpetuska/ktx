package dev.petuska.ktx.service

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import okio.Buffer
import org.junit.jupiter.api.Test
import org.koin.test.inject
import test.util.ITest

class FileServiceTest : ITest() {
  private val target: FileService by inject()

  private val content = "TEST CONTENT"

  @Test
  fun linkBinTest() = runTest {
    val linkTarget = target.writeScript("target", Buffer())
    target.linkBin("test.kts", linkTarget).should {
      fileSystem.exists(it).shouldBeTrue()
      fileSystem.metadata(it).symlinkTarget shouldBe linkTarget
    }
  }

  @Test
  fun writeCacheTest() = runTest {
    target.writeCache("test.kts", Buffer().writeUtf8(content)).should {
      fileSystem.exists(it).shouldBeTrue()
      fileSystem.read(it) { readUtf8() } shouldBe content
    }
  }

  @Test
  fun writeScriptTest() = runTest {
    target.writeScript("test.kts", Buffer().writeUtf8(content)).should {
      fileSystem.exists(it).shouldBeTrue()
      fileSystem.read(it) { readUtf8() } shouldBe content
    }
  }
}
