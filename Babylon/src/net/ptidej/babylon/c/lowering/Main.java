package net.ptidej.babylon.c.lowering;

import java.lang.reflect.Method;

import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.OpTransformer;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method distanceMethod = Main.class.getDeclaredMethod("distance", double.class, double.class);
		System.out.println(distanceMethod);

		final CoreOp.FuncOp distanceFuncOp = Op.ofMethod(distanceMethod).get();
		System.out.println(distanceFuncOp.toText());

		final CoreOp.FuncOp distanceFuncOpLowered = distanceFuncOp.transform(OpTransformer.LOWERING_TRANSFORMER);
		System.out.println(distanceFuncOpLowered.toText());
	}

	@CodeReflection
	public static double distance(final double a, final double b) {
		final double diff = a - b;
		// Note, incorrect for negative zero values
		final double result = diff < 0d ? -diff : diff;
		return result;
	}
}
