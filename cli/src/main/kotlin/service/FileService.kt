package dev.petuska.ktx.service

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.io.File

@Single
class FileService(
  private val dirService: DirService,
) {
  suspend fun writeBin(name: String, content: ByteReadChannel, force: Boolean = false) =
    write(target = dirService.bin.resolve(name), content = content, executable = true, force = force)

  suspend fun writeCache(name: String, content: ByteReadChannel, force: Boolean = false) =
    write(target = dirService.cache.resolve(name), content = content, executable = true, force = force)

  fun getCache(name: String): File? = dirService.cache.resolve(name).takeIf(File::exists)

  suspend fun writeJar(name: String, content: ByteReadChannel, force: Boolean = false) =
    write(target = dirService.jars.resolve(name), content = content, executable = true, force = force)

  suspend fun writeScript(name: String, content: ByteReadChannel, force: Boolean = false) =
    write(target = dirService.scripts.resolve(name), content = content, executable = true, force = force)

  suspend fun copyScript(source: File, force: Boolean = false) =
    writeScript(name = source.name, content = source.readChannel(), force = force)

  private suspend fun write(target: File, content: ByteReadChannel, executable: Boolean, force: Boolean): File {
    require(force || !target.exists()) { "${target.absolutePath} already exists!" }
    withContext(Dispatchers.IO) {
      target.parentFile.mkdirs()
      target.createNewFile()
      target.writeChannel().use { content.copyTo(this) }
      target.setExecutable(executable)
    }
    return target
  }
}
