package net.ptidej.isthmus;

import java.util.function.BiFunction;

public class Syntax {
	public static void main(final String[] args) {
		interface Executable {
			int myMax(final int a, final int b);
		}
		class MyExecutable implements Executable {
			public int myMax(final int a, final int b) {
				return Math.max(a, b);
			}
		}
		final MyExecutable max1 = new MyExecutable();
		System.out.println(max1.myMax(1, 2));
		System.out.println(max1);

		class MySecondExecutable implements BiFunction<Integer, Integer, Integer> {
			public Integer apply(final Integer a, final Integer b) {
				return Math.max(a, b);
			}
		}
		final MySecondExecutable max2 = new MySecondExecutable();
		Syntax.methodNeedingSomeComparator(max2);

		final BiFunction<Integer, Integer, Integer> max3 = new BiFunction<Integer, Integer, Integer>() {
			public Integer apply(final Integer a, final Integer b) {
				return Math.max(a, b);
			}
		};
		Syntax.methodNeedingSomeComparator(max3);

		final BiFunction<Integer, Integer, Integer> max4 = (final Integer a, final Integer b) -> {
			return Math.max(a, b);
		};
		Syntax.methodNeedingSomeComparator(max4);

		final BiFunction<Integer, Integer, Integer> max5 = Math::max;
		Syntax.methodNeedingSomeComparator(max5);
	}

	static void methodNeedingSomeComparator(final BiFunction<Integer, Integer, Integer> aComparator) {
		System.out.println(aComparator.apply(1, 2));
		System.out.println(aComparator);
	}
}
