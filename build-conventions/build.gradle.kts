import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  mavenLocal()
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
  implementation("org.jetbrains.kotlin:kotlin-serialization:_")
  implementation("com.github.jakemarsden:git-hooks-gradle-plugin:_")
  implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:_")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:_")
  implementation("dev.petuska:klip-gradle-plugin:_")
  implementation("io.sdkman:gradle-sdkvendor-plugin:_")
  implementation("com.autonomousapps:dependency-analysis-gradle-plugin:_")
}

tasks {
  withType<KotlinCompile> {
    compilerOptions {
      languageVersion.set(KotlinVersion.KOTLIN_1_9)
    }
  }
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
