package dev.petuska.ktx.service

import dev.petuska.ktx.domain.TargetKind
import dev.petuska.ktx.util.isUrl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.openZip
import org.koin.core.annotation.Single

@Single
class ResourceService(
  private val fileService: FileService,
  private val httpClient: HttpClient,
  private val fileSystem: FileSystem,
) {
  suspend fun resolve(target: String, kind: TargetKind?): Path {
    val script: Path = when (kind ?: TargetKind.guess(target)) {
      TargetKind.PACKAGE -> cacheRemotePackage(target)
      TargetKind.SCRIPT -> if (target.isUrl()) cacheRemoteResource(target) else target.toPath()
    }
    require(fileSystem.exists(script)) { "Failed to cache $target resource to $script" }
    return script
  }

  private suspend fun cacheRemotePackage(coordinates: String): Path {
    val name = coordinates.replace(":", "__")
    return fileService.getCache(name) ?: run {
      val (group, artefact, version) = coordinates.split(":")
      val repo = "https://repo.maven.apache.org/maven2"
      val jarUrl = repo + "/$group/$artefact".replace(".", "/") + "/$version/$artefact-$version.jar"
      val jar = cacheRemoteResource(jarUrl)
      val zipTree = fileSystem.openZip(jar)
      val manifestPath = "/META-INF/MANIFEST.MF".toPath()
      require(zipTree.exists(manifestPath)) { "$jarUrl is missing $manifestPath and therefore is not executable" }
      val mainClass = zipTree.read(manifestPath) { readUtf8() }.split("\n").first { it.startsWith("Main-Class") }
        .substringAfter("Main-Class:").trim()
      fileService.writeCache(
        name = name,
        force = true,
        content = Buffer().writeUtf8(
          """
          @file:DependsOn("$coordinates")
          $mainClass.main(args)
          """.trimIndent()
        )
      )
    }
  }

  private suspend fun cacheRemoteResource(target: String): Path {
    val name = target.split("/").last()
    return fileService.getCache(name) ?: fileService.writeCache(
      name = name,
      force = true,
      content = Buffer().readFrom(httpClient.get(target) { expectSuccess = true }.bodyAsChannel().toInputStream()),
    )
  }
}
