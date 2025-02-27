package net.ptidej.panama.jni;

public class Main {
	static {
		System.loadLibrary("net_ptidej_panama_jni_Main");
	}

	public static native int stringLength(final String s);

	public static void main(final String[] args) {
		final String s = "Hello, world!";
		System.out.println(Main.stringLength(s));
	}
}
