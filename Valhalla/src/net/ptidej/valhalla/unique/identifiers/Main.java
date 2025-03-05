package net.ptidej.valhalla.unique.identifiers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
		final int i1 = 42;
		final int j1 = 42;
		System.out.println(i1 == j1);

		final Integer i2 = Integer.valueOf(42);
		final Integer j2 = Integer.valueOf("42");
		System.out.println(i2 == j2);

		System.out.println(System.identityHashCode(i2));
		System.out.println(System.identityHashCode(j2));

		final String s1 = "Hello, World!";
		final String s2 = "Hello, Wordl!";
		System.out.println(s1 == s2);

		final ClassLoader cl1 = Main.class.getClassLoader();
		final Class<?> c1 = cl1.loadClass("net.ptidej.valhalla.unique.identifiers.Main");
		final ClassLoader cl2 = Main.class.getClassLoader();
		final Class<?> c2 = cl2.loadClass("net.ptidej.valhalla.unique.identifiers.Main");
		final MyClassLoader cl3 = new MyClassLoader();
		final Class<?> c3 = cl3.findClass("net.ptidej.valhalla.unique.identifiers.Main");
		System.out.println(c1 == c2);
		System.out.println(c1 == c3);
	}

	private static class MyClassLoader extends ClassLoader {
		@Override
		public Class<?> findClass(final String name) throws ClassNotFoundException {
			final byte[] b = this.loadClassFromFile(name);
			return super.defineClass(name, b, 0, b.length);
		}

		private byte[] loadClassFromFile(final String fileName) {
			final InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(fileName.replace('.', File.separatorChar) + ".class");
			final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			int nextValue = 0;
			try {
				while ((nextValue = inputStream.read()) != -1) {
					byteStream.write(nextValue);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return byteStream.toByteArray();
		}
	}

}
