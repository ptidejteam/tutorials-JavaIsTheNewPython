package net.ptidej.valhalla.valueclass;

import java.lang.reflect.Modifier;

public class Main {

	public static void main(final String[] args) {
		final Substring subs1 = new Substring("Hello, World!", 7, 13);
		System.out.println(subs1);
		
		System.out.println(Modifier.isIdentity(Substring.class.getModifiers()));
	}

}
