import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("convention.common")
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
}

dependencies {
  testImplementation("io.kotest:kotest-assertions-core:_")
}

java {
  targetCompatibility = JavaVersion.VERSION_11
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
  withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(provider { JvmTarget.fromTarget("${java.targetCompatibility}") })
    }
  }
}
