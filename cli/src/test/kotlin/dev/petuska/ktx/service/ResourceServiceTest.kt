package dev.petuska.ktx.service

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import org.junit.jupiter.api.Test
import org.koin.test.inject
import test.util.ITest

class ResourceServiceTest : ITest() {
  private val target: ResourceService by inject()

  @Test
  fun resolveRemoteResource() = runTest {
    target.resolve("https://raw.githubusercontent.com/mpetuska/ktx/master/README.md").should {
      fileSystem.exists(it).shouldBeTrue()
      fileSystem.read(it) { readUtf8() }.shouldNotBeEmpty()
    }
  }

  @Test
  fun resolveLocalResource() = runTest {
    val file = extractTempResource("/test-script.main.kts".toPath())
    target.resolve(file.toString()).should {
      fileSystem.exists(it).shouldBeTrue()
      it shouldBe file
    }
  }
}
