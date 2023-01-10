package dev.petuska.ktx.service

import dev.petuska.ktx.util.isUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.annotation.Single

@Single
class ResourceService(
  private val fileService: FileService,
  private val httpClient: HttpClient,
  private val fileSystem: FileSystem,
) {
  suspend fun resolve(target: String): Path {
    val file = if (target.isUrl()) {
      val name = target.split("/").last()

      fileService.getCache(name) ?: fileService.writeCache(
        name = name,
        content = Buffer().readFrom(httpClient.get(target).bodyAsChannel().toInputStream()),
        force = true
      )
    } else {
      target.toPath()
    }
    require(fileSystem.exists(file)) { "Failed to cache $target resource to ${file.toFile()}" }
    return file
  }
}
