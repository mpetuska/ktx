plugins {
  id("convention.kjvm-app")
  alias(libs.plugins.ksp)
  alias(libs.plugins.sdkman)
}

dependencies {
  implementation(kotlin("scripting-jvm-host"))
  implementation(kotlin("main-kts"))
  implementation(libs.clikt)
  implementation(libs.ktor.client.cio)
  implementation(libs.okio)

  implementation(libs.koin.core)
  implementation(libs.koin.slf4j)
  implementation(libs.koin.annotations)
  ksp(libs.koin.ksp)

  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.koin.test.junit5)
  testImplementation(libs.okio.fakefilesystem)
}

kotlin {
  sourceSets {
    test {
      languageSettings{
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      }
    }
  }
}

application {
  mainClass = "dev.petuska.ktx.MainKt"
  applicationName = rootProject.name
}

sdkman {
  candidate = rootProject.name
  version = "${project.version}"
  url = "https://github.com/mpetuska/ktx/releases/download/${project.version}/ktx-${project.version}.zip"
  hashtag = rootProject.name
}

tasks {
  register("processDist", Copy::class) {
    inputs.property("version", version)
    destinationDir = layout.buildDirectory.dir("resources/dist").get().asFile
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
