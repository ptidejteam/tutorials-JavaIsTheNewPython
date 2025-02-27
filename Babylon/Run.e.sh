javac -cp bin/ -d bin/ src/net/ptidej/babylon/e/interpreter/Main.java
java --add-opens jdk.incubator.code/jdk.incubator.code.op=ALL-UNNAMED -cp bin net.ptidej.babylon.e.interpreter.Main

