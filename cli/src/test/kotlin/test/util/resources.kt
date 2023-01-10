package test.util

import okio.FileSystem
import okio.Path
import okio.Source
import okio.buffer

fun findResource(path: Path): Source = FileSystem.RESOURCES.run {
  if (exists(path)) source(path) else error("Unable to find resource [$path] on classpath")
}

fun readResource(path: Path): ByteArray = findResource(path).buffer().readByteArray()
