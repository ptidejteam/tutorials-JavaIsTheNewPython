package net.ptidej.isthmus.datatypes;

public class Strings {
	public static void main(java.lang.String[] args) {
		final String string1 = "Hello";
		final String string2 = ", World!";

		final String string3 = string1 + string2;
		System.out.println(string3);

		final StringBuilder string4 = new StringBuilder(string3);
		string4.setCharAt(0, 'Y');
		System.out.println(string4);
	}
}
