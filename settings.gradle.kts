plugins {
  id("de.fayard.refreshVersions") version "0.40.2"
  id("com.gradle.enterprise") version "3.10.3"
}

refreshVersions {
  versionsPropertiesFile = rootDir.resolve("gradle/versions.properties")
  extraArtifactVersionKeyRules(rootDir.resolve("gradle/versions.rules"))
}

includeBuild("./build-conventions")
rootProject.name = "ktx"

include(
  ":cli",
)
