plugins {
  id("convention.kjvm-app")
  id("com.google.devtools.ksp")
  id("io.sdkman.vendors")
}

dependencies {
  implementation(kotlin("scripting-jvm-host"))
  implementation(kotlin("main-kts"))
  implementation("com.github.ajalt.clikt:clikt:_")
  implementation("io.ktor:ktor-client-cio:_")
  implementation("com.squareup.okio:okio:_")

  implementation("io.insert-koin:koin-core:3.3.2")
  implementation("io.insert-koin:koin-logger-slf4j:3.3.0")
  implementation("io.insert-koin:koin-annotations:1.1.0")
  ksp("io.insert-koin:koin-ksp-compiler:1.1.0")

  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
  testImplementation("io.insert-koin:koin-test-junit5:3.3.2")
  testImplementation("com.squareup.okio:okio-fakefilesystem:_")
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

sdkman {
  candidate.set(rootProject.name)
  version.set(rootProject.version.toString())
  url.set("https://github.com/mpetuska/ktx/releases/download/$version/ktx.zip")
  hashtag.set(rootProject.name)

  consumerKey.set("TODO")
  consumerToken.set("TODO")
}

tasks {
  register("processDist", Copy::class) {
    inputs.property("version", version)
    destinationDir = buildDir.resolve("resources/dist")
    from(projectDir.resolve("src/main/dist"))
    doLast {
      destinationDir.resolve(".version").writeText("$version")
    }
  }
  processResources {
    inputs.property("version", version)
    from(rootDir.resolve("LICENSE"))
    doLast {
      destinationDir.resolve(".version").writeText("$version")
    }
  }
  afterEvaluate {
    named("explodeCodeSourceMain") { dependsOn("kspKotlin") }
  }
}

distributions {
  main {
    contents {
      from(tasks.named("processDist")) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }
      from(rootDir.resolve("LICENSE"))
    }
  }
}

idea {
  module {
    resourceDirs.add(rootDir.resolve("src/main/dist"))
  }
}
