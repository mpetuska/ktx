plugins {
  if (System.getenv("CI") == null) id("convention.git-hooks")
  id("convention.common")
  id("dependency-analysis")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
