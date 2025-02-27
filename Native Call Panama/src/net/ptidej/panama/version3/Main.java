package net.ptidej.panama.version3;

import static net.ptidej.panama.version3.net_ptidej_panama_jextract_strlen_h.strlen;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class Main {
	public static void main(final String[] args) throws Throwable {
		try (final Arena arena = Arena.ofConfined()) {
			final MemorySegment cString = arena.allocateFrom("Hello, World!");
			System.out.println(strlen(cString));
		}
	}
}