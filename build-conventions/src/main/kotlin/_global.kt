import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal inline val Project.libs get() = the<LibrariesForLibs>()
