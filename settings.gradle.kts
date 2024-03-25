plugins {
  id("com.gradle.enterprise") version "3.16.2"
}

includeBuild("./build-conventions")
rootProject.name = "ktx"

include(
  ":cli",
)
