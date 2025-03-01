package net.ptidej.isthmus.constructor.contexts;

public class Main {
	public static void main(final String[] args) {
		final B b = new B(42);
		System.out.println(b);
	}
}

class A {
}

class B extends A {
	private String s;
	private int i;

	static {
		System.out.println("Loading of B...");
	}

	{
		System.out.println("Building of a B...");
		this.s = "Omega";
	}

	B(final int a) {
		System.out.println(a);
		this.s = "Alpha";
		super();
		this.i = a;
	}

	@Override
	public String toString() {
		return this.s + " " + this.i;
	}
}