plugins {
  `kotlin-dsl`
}

repositories {
  mavenLocal()
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

  implementation(libs.plugin.versions)
  implementation(libs.plugin.versions.update)
  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.kotlin.serialization)
  implementation(libs.plugin.git.hooks)
  implementation(libs.plugin.detekt)
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
