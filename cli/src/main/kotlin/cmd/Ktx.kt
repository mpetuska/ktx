package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Single
class Ktx : NoOpCliktCommand(
  invokeWithoutSubcommand = true,
  printHelpOnEmptyArgs = true,
), KoinComponent {
  init {
    subcommands(
      get<Clean>(),
      get<Run>(),
      get<Install>(),
      CompletionCommand(),
    )
  }
}
