package dev.petuska.ktx

import dev.petuska.ktx.cmd.Ktx

fun main(args: Array<String>) {
  println("KTX Args: ${args.toList()}")
  Ktx().main(args)
}
