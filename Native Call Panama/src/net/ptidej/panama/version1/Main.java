package net.ptidej.panama.version1;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Main {
	public static void main(final String[] args) throws Throwable {
		final Linker linker = Linker.nativeLinker();
		final SymbolLookup dll = SymbolLookup.libraryLookup("net_ptidej_panama_version2_Main.dll", Arena.global());
		final MethodHandle strlen = linker.downcallHandle(dll.find("stringLength").orElseThrow(),
				FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

		try (final Arena arena = Arena.ofConfined()) {
			final MemorySegment cString = arena.allocateFrom("Hello, World!");
			final long length = (long) strlen.invoke(cString);
			System.out.println(length);
		}
	}
}