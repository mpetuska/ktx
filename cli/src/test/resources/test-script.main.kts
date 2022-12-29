println("SCRIPTING!")
println("Script args: ${args.toList()}")
if (args.contains("error")) error("EXPECTED SCRIPT ERROR")
