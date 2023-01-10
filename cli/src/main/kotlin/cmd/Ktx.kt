package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent

@Single
class Ktx : NoOpCliktCommand(
  invokeWithoutSubcommand = true,
  printHelpOnEmptyArgs = true,
), KoinComponent {
  init {
    subcommands(
      getKoin().getAll<CliktCommand>().filterNot { it::class == this::class } + CompletionCommand(),
    )
  }
}
