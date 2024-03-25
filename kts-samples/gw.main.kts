#!/usr/bin/env -S ktx execute
/**
 * A convenience wrapper script that finds the nearest `gradlew` script
 * and appends gradle project path relative to it to all task arguments.</p>
 *
 * Supports running from any project directory.</p>
 *
 * e.g.:</p>
 * All commands are executed from project's root directory under the hood.
 * Given the following file tree and executing `gw tasks -Pproperty="69"` will
 * result in different behaviour depending on your working directory.
 * ```
 * project
 * ├── moduleA
 * │   ├── src
 * │   │   ├── main
 * │   │   │   ├── kotlin
 * │   │   │   │   └── Main.kt
 * │   │   │   └── resources
 * │   │   │       └── ...
 * │   │   └── test
 * │   │       └── ...
 * │   └── build.gradle
 * ├── moduleB
 * │   └── src
 * │       ├── main
 * │       │   ├── java
 * │       │   │   └── Main.java
 * │       │   └── resources
 * │       │       └── ...
 * │       └── test
 * │           └── ...
 * ├── gradle
 * │   └── ...
 * └── gradlew
 * ```
 * | PWD                         | Executed Command                        |
 * |-----------------------------|-----------------------------------------|
 * | /project                    | /gradlew tasks -Pproperty="69"          |
 * | /moduleA                    | /gradlew :moduleA:tasks -Pproperty="69" |
 * | /moduleA/src/main/resources | /gradlew :moduleA:tasks -Pproperty="69" |
 * | /moduleB                    | /gradlew :moduleB:tasks -Pproperty="69" |
 * | /moduleB/src/main/java      | /gradlew :moduleB:tasks -Pproperty="69" |
 */
import java.io.File
import kotlin.system.exitProcess

val debug = System.getenv("DEBUG") !in arrayOf(null, "false")
val root = File("/")
val pwd = File(System.getProperty("user.dir"))
var wrapper = pwd.resolve("gradlew")
var buildFile: File? = pwd.listFiles()?.firstOrNull { it.name.startsWith("build.gradle") }

while (!wrapper.exists() && wrapper.parentFile != root) {
  wrapper = wrapper.parentFile.parentFile.resolve("gradlew")
  buildFile = buildFile ?: wrapper.parentFile.listFiles()?.firstOrNull { it.name.startsWith("build.gradle") }
}

if (wrapper.exists()) {
  val projectDir = (buildFile ?: wrapper).parentFile
  val project = if (wrapper.parentFile == projectDir) {
    ""
  } else {
    projectDir.absolutePath.substringAfter(wrapper.parentFile.absolutePath)
      .replace("/", ":").plus(":")
  }
  val arguments = args.map { arg ->
    if (arg.startsWith("-")) arg else "$project$arg"
  }
  if (debug) println("Running [$wrapper, $arguments] from ${wrapper.parentFile}")
  @Suppress("SpreadOperator")
  ProcessBuilder("$wrapper", *arguments.toTypedArray())
    .inheritIO()
    .directory(wrapper.parentFile)
    .start().waitFor().let(::exitProcess)
} else {
  error("Unable to find gradle wrapper in the file tree")
}
