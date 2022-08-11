#!/usr/bin/env kotlin
println("SCRIPTING!")
println(args.toList())
if (args.contains("error")) error("EXPECTED SCRIPT ERROR")
