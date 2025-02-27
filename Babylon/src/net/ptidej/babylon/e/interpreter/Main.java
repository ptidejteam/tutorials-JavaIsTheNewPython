package net.ptidej.babylon.e.interpreter;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.analysis.SSA;
import jdk.incubator.code.interpreter.Interpreter;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method distance1Method = Main.class.getDeclaredMethod("distance", double.class, double.class);
		System.out.println(distance1Method);

		final CoreOp.FuncOp distance1FuncOp = Op.ofMethod(distance1Method).get();
		// System.out.println(distance1FuncOp.toText());
		final Double resultsInterpreted1 = (Double) Interpreter.invoke(MethodHandles.lookup(), distance1FuncOp, 10, 2);
		System.out.println(resultsInterpreted1);

		final CoreOp.FuncOp distance1FuncOpSSA = SSA.transform(distance1FuncOp);
		// System.out.println(distance1FuncOpSSA.toText());
		final Double resultsInterpreted2 = (Double) Interpreter.invoke(MethodHandles.lookup(), distance1FuncOpSSA, 42,
				2);
		System.out.println(resultsInterpreted2);
	}

	@CodeReflection
	public static double distance(final double a, final double b) {
		return Math.abs(a - b);
	}
}
