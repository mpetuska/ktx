plugins {
  id("convention.common")
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("org.beryx.runtime")
}

kotlin {
  sourceSets {
    test {
      dependencies {
        implementation("dev.petuska:klip:_")
      }
    }
  }
}
