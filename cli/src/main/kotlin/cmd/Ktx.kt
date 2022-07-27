package dev.petuska.ktx.cmd

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands

class Ktx : NoOpCliktCommand(
  invokeWithoutSubcommand = true,
  printHelpOnEmptyArgs = true,
) {
  init {
    subcommands(
      Run(),
      CompletionCommand(),
    )
  }
}
