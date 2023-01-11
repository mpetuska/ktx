package dev.petuska.ktx.service

import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single

@Single
class DirService(
  @Property("KTX_DIR")
  ktxHome: String?,
  private val fileSystem: FileSystem,
) {
  val home = (ktxHome ?: "${System.getProperty("user.home")}/.ktx").toPath()
  val cache = home.resolve("cache")
  val bin = home.resolve("bin")
  val scripts = home.resolve("scripts")

  val bashrc = ("${System.getProperty("user.home")}/.bashrc").toPath().takeIf(fileSystem::exists)
  val zshrc = ("${System.getProperty("user.home")}/.zshrc").toPath().takeIf(fileSystem::exists)
  val profile = ("${System.getProperty("user.home")}/.profile").toPath().takeIf(fileSystem::exists)
}
