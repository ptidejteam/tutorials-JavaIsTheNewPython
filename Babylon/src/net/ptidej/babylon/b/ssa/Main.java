package net.ptidej.babylon.b.ssa;

import java.lang.reflect.Method;

import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.analysis.SSA;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method distance1Method = Main.class.getDeclaredMethod("distance1", double.class, double.class);
		System.out.println(distance1Method);

		final Method distance2Method = Main.class.getDeclaredMethod("distance2", double.class, double.class);
		System.out.println(distance2Method);

		final CoreOp.FuncOp distance1FuncOp = Op.ofMethod(distance1Method).get();
		System.out.println(distance1FuncOp.toText());

		final CoreOp.FuncOp distance2FuncOp = Op.ofMethod(distance2Method).get();
		System.out.println(distance2FuncOp.toText());

		final CoreOp.FuncOp distance1FuncOpSSA = SSA.transform(distance1FuncOp);
		System.out.println(distance1FuncOpSSA.toText());

		final CoreOp.FuncOp distance2FuncOpSSA = SSA.transform(distance2FuncOp);
		System.out.println(distance2FuncOpSSA.toText());
	}

	@CodeReflection
	public static double distance1(final double a, final double b) {
		return Math.abs(a - b);
	}

	@CodeReflection
	public static double distance2(final double a, final double b) {
		final double diff = a - b;
		final double result = Math.abs(diff);
		return result;
	}
}
