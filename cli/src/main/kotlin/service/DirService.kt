package dev.petuska.ktx.service

import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import java.io.File

@Single
class DirService(
  @Property("KTX_HOME")
  ktxHome: String?,
) {
  val home = File(ktxHome ?: "${System.getProperty("user.home")}/.ktx")
  val cache: File = home.resolve("cache")
  val bin: File = home.resolve("bin")
  val scripts: File = home.resolve("scripts")
  val jars: File = home.resolve("jars")
}
