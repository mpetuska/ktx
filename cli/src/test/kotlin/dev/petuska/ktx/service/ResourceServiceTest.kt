package dev.petuska.ktx.service

import dev.petuska.ktx.test.util.extractTempResource
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.ksp.generated.module
import org.koin.test.junit5.KoinTestExtension
import java.io.File

class ResourceServiceTest {
  @RegisterExtension
  val koinTestRule = KoinTestExtension.create {
    modules(ServicesModule.module)
  }

  @field:TempDir
  lateinit var home: File
  private val dirService: DirService get() = DirService(home.absolutePath)

  private val target get() = ResourceService(FileService(dirService), HttpClient())

  @Test
  fun resolveRemoteResource() = runTest {
    val result = target.resolve("https://raw.githubusercontent.com/mpetuska/ktx/master/README.md")
    result.shouldExist()
    result.shouldNotBeEmpty()
  }

  @Test
  fun resolveLocalResource() = runTest {
    val file = extractTempResource("/test-script.main.kts")
    val result = target.resolve(file.absolutePath)
    result.shouldExist()
    result shouldBe file
  }
}
