@file:DependsOn("dev.petuska:kon-jvm:1.1.4")

println("SCRIPTING!")
println("Script args: ${args.toList()}")
dev.petuska.kon.core.kobj {
  "nice?" to 69
}.let(::println)
if (args.contains("error")) error("EXPECTED SCRIPT ERROR")
