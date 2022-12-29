plugins {
  id("convention.common")
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("org.beryx.runtime")
}

dependencies {
//  testImplementation(kotlin("test-junit5"))
  testImplementation("io.kotest:kotest-assertions-core:_")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }
}
