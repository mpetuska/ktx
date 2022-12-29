package dev.petuska.ktx.service

import dev.petuska.ktx.util.isUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.koin.core.annotation.Single
import java.io.File

@Single
class ResourceService(
  private val fileService: FileService,
  private val httpClient: HttpClient,
) {
  suspend fun resolve(target: String): File {
    val file = if (target.isUrl()) {
      val name = target.split("/").last()
      fileService.getCache(name) ?: fileService.writeCache(name, httpClient.get(target).bodyAsChannel(), true)
    } else {
      File(target)
    }
    require(file.exists()) { "Failed to cache $target resource to $file" }
    return file
  }
}
