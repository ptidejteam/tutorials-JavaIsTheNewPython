/**
 * https://www.reddit.com/r/java/comments/1noc7u/friendly_reminder_about_integer_int_nulls_and/
 */

package net.ptidej.valhalla.autoboxing.gotchas;

import java.util.Vector;

public class Main {

	private static void f(final Object a, final boolean b) {
		System.out.println("f(Object a, boolean b)");
	}

	private static void f(final Object a, final Object b) {
		System.out.println("f(Object a, Object b)");
	}

	public static void main(final String[] args) {
		try {
			final Integer a = null;
			System.out.println("a = " + a);
			final int b = a;
			System.out.println("b = " + b);
		} catch (final NullPointerException e) {
		}
		{
			// final ResultSet rs = this.doSomeDatabaseQuery();
			// final Double value = rs.getDouble("someDoubleField");
			// Should be:
			// ResultSet rs = this.doSomeDatabaseQuery();
			// double tmp = rs.getDouble("someDoubleField");
			// Double value = rs.wasNull() ? null : Double.valueOf(foo);
			// Or:
			// ResultSet rs = this.doSomeDatabaseQuery();
			// Double value = (Double) rs.getObject("someDoubleField");
		}
		{
			final Integer i = new Integer(1);
			final Double d = new Double(2.0);

			System.out.println("i  = " + i);
			System.out.println("d  = " + d);

			final Object o1 = i;
			System.out.println("o1 = " + o1);

			final Object o2 = true ? i : d;
			System.out.println("o2 = " + o2);

			System.out.println(o1.equals(o2));
		}
		{
			final int smallNumberInt = 42;
			if (smallNumberInt == 42)
				System.out.println("1.");
			final Integer smallNumberInteger = Integer.valueOf(smallNumberInt);
			if (smallNumberInteger == (Object) 42)
				System.out.println("2.");

			final int largeNumberInt = 500;
			if (largeNumberInt == 500)
				System.out.println("3.");
			final Integer largeNumberInteger = Integer.valueOf(largeNumberInt);
			if (largeNumberInteger == (Object) 500)
				System.out.println("4.");
		}
		{
			final int value = 42;
			final Vector v = new Vector();
			v.add(value);
			System.out.println("first = " + v.getFirst().getClass());
			// Main.f(value, false); // Ambiguous
		}
	}

}
