package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.core.CliktCommand
import dev.petuska.ktx.service.SystemService
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
class Version : CliktCommand(
  help = "Prints details about the current ktx version",
), KoinComponent {
  private val systemService: SystemService by inject()

  override fun run() {
    echo("Version: ${systemService.version}")
  }
}
