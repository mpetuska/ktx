import java.util.Properties

fun resolveProperties(file: File) = file.takeIf(File::exists)?.let {
  Properties().apply {
    it.inputStream().use(::load)
  }.mapKeys { (k, _) -> k.toString() }
}?.toList()?.forEach { (k, v) ->
  project.extra[k] = v
}

rootDir.resolve("local.properties")
projectDir.resolve("local.properties")
