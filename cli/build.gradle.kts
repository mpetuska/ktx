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
      }
    }
  }
}

application {
  mainClass.set("dev.petuska.ktx.MainKt")
}

runtime {
  jpackage {
    imageName = rootProject.name
    skipInstaller = true
    installerType = "rpm"
    installerName = imageName
  }
}
