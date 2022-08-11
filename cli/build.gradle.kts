plugins {
  id("convention.kjvm-app")
}

kotlin {
  sourceSets {
    main {
      dependencies {
        implementation("com.github.ajalt.clikt:clikt:_")
        implementation(kotlin("scripting-jvm-host"))
        implementation(kotlin("main-kts"))
        implementation("io.ktor:ktor-client-cio:_")
      }
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
