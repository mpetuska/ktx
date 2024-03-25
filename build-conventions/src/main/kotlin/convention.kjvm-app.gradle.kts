import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("convention.common")
  kotlin("jvm")
  kotlin("plugin.serialization")
  application
}

dependencies {
  testImplementation(libs.kotest.assertions.core)
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
      jvmTarget = JvmTarget.JVM_11
    }
  }
}
