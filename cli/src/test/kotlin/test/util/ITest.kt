package test.util

import dev.petuska.ktx.config.utilKoin
import dev.petuska.ktx.service.DirService
import dev.petuska.ktx.service.ServicesModule
import okio.FileSystem
import okio.Path
import okio.fakefilesystem.FakeFileSystem
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.io.File

abstract class ITest : KoinTest {
  @field:TempDir
  protected lateinit var home: File

  @RegisterExtension
  protected open val koinExtension = KoinTestExtension.create {
    allowOverride(true)
    modules(ServicesModule.module, utilKoin, module {
      single { FakeFileSystem() }
      single { DirService(home.absolutePath, get()) }
    })
  }

  protected val fileSystem: FileSystem by inject()

  protected fun extractTempResource(path: Path): Path {
    val content = readResource(path)
    val target = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve(path.name)
    target.parent?.let(fileSystem::createDirectories)
    fileSystem.write(target) {
      write(content)
    }
    return target
  }
}
