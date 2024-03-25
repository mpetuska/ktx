package dev.petuska.ktx.service

import okio.FileSystem
import okio.Path
import okio.Source
import org.koin.core.annotation.Single

@Single
class FileService(
  private val dirService: DirService,
  private val fileSystem: FileSystem,
) {
  fun linkBin(name: String, target: Path, force: Boolean = false): Path {
    val link = dirService.bin.resolve(name)
    if (force) fileSystem.delete(link)
    link.parent?.let(fileSystem::createDirectories)
    fileSystem.createSymlink(link, target)
    return link
  }

  fun writeBin(name: String, content: Source, force: Boolean = false): Path =
    write(target = dirService.bin.resolve(name), content = content, force = force)

  fun writeCache(name: String, content: Source, force: Boolean = false) =
    write(target = dirService.cache.resolve(name), content = content, force = force)

  fun getCache(name: String): Path? = dirService.cache.resolve(name).takeIf(fileSystem::exists)

  fun writeScript(name: String, content: Source, force: Boolean = false) =
    write(target = dirService.scripts.resolve(name), content = content, force = force)

  private fun write(target: Path, content: Source, force: Boolean): Path {
    if (force) fileSystem.delete(target)
    target.parent?.let(fileSystem::createDirectories)
    fileSystem.write(target, true) {
      writeAll(content)
    }
    return target
  }
}
