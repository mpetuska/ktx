package dev.petuska.ktx.test.util

import java.io.File
import java.net.URL
import java.nio.file.Files

fun Any.findResource(path: String): URL = this::class.java.getResource(path)
  ?: error("Unable to find resource [$path] on classpath")

fun Any.readResource(path: String): ByteArray = findResource(path).readBytes()

fun Any.extractTempResource(path: String): File {
  val content = readResource(path)
  return Files.createTempFile("ktx", "test").toFile().apply {
    writeBytes(content)
    deleteOnExit()
  }
}

fun File.clear() = listFiles()?.forEach(File::deleteRecursively)
