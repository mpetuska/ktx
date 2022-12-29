plugins {
  id("convention.kjvm-app")
  id("com.google.devtools.ksp")
}

dependencies {
  implementation(kotlin("scripting-jvm-host"))
  implementation(kotlin("main-kts"))
  implementation("com.github.ajalt.clikt:clikt:_")
  implementation("io.ktor:ktor-client-cio:_")

  implementation("io.insert-koin:koin-core:3.3.2")
  implementation("io.insert-koin:koin-logger-slf4j:3.3.0")
  implementation("io.insert-koin:koin-annotations:1.1.0")
  ksp("io.insert-koin:koin-ksp-compiler:1.1.0")

  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
  testImplementation("io.insert-koin:koin-test-junit5:3.3.2")
}

sourceSets {
  main {
    java.srcDirs(buildDir.resolve("generated/ksp/main/kotlin"))
  }
}

kotlin {
  sourceSets {
    test {
      languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

application {
  mainClass.set("dev.petuska.ktx.MainKt")
  applicationName = rootProject.name
}

runtime {
  jpackage {
    imageName = rootProject.name
    skipInstaller = true
    installerType = "rpm"
    installerName = imageName
  }
}
