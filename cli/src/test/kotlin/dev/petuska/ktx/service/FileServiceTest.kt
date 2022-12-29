package dev.petuska.ktx.service

import io.kotest.matchers.file.shouldBeExecutable
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import java.io.File

class FileServiceTest : KoinTest {
  @RegisterExtension
  val koinTestRule = KoinTestExtension.create {
    modules(ServicesModule.module)
  }

  @field:TempDir
  lateinit var home: File

  private val dirService: DirService get() = DirService(home.absolutePath)

  private val content = "TEST CONTENT"

  @Test
  fun writeBinTest() = runTest {
    val target = FileService(dirService)
    val result = target.writeBin("test.kts", ByteReadChannel(content.toByteArray()))
    result.shouldExist()
    result.readText() shouldBe content
    result.shouldBeExecutable()
  }

  @Test
  fun writeCacheTest() = runTest {
    val target = FileService(dirService)
    val result = target.writeCache("test.kts", ByteReadChannel(content.toByteArray()))
    result.shouldExist()
    result.readText() shouldBe content
  }

  @Test
  fun writeScriptTest() = runTest {
    val target = FileService(dirService)
    val result = target.writeCache("test.kts", ByteReadChannel(content.toByteArray()))
    result.shouldExist()
    result.readText() shouldBe content
  }
}
