package dev.petuska.ktx.domain

enum class TargetKind {
  SCRIPT, PACKAGE;

  companion object {
    fun guess(target: String): TargetKind = when {
      target.split(":").size == 3 && !target.contains("/") -> PACKAGE
      else -> SCRIPT
    }
  }
}
