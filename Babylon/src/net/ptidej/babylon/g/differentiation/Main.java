package net.ptidej.babylon.g.differentiation;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import jdk.incubator.code.Block;
import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.analysis.SSA;
import jdk.incubator.code.interpreter.Interpreter;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method f = Main.class.getDeclaredMethod("f", double.class, double.class);

		final Double result_f1 = f(2, 3);
		System.out.println(result_f1);
		final CoreOp.FuncOp funcOp_f = Op.ofMethod(f).get();
		final CoreOp.FuncOp funcOp_f_SSA = SSA.transform(funcOp_f);
		final Double result_f2 = (Double) Interpreter.invoke(MethodHandles.lookup(), funcOp_f_SSA, 2, 3);
		System.out.println(result_f2);

		final Double result_df_dx1 = df_dx(2, 3);
		System.out.println(result_df_dx1);
		final Block.Parameter x = funcOp_f_SSA.body().entryBlock().parameters().get(0);
		final CoreOp.FuncOp funcOp_df_dx = ExpressionElimination
				.eliminate(ForwardDifferentiation.partialDiff(funcOp_f_SSA, x));
		final Double result_df_dx2 = (Double) Interpreter.invoke(MethodHandles.lookup(), funcOp_df_dx, 2, 3);
		System.out.println(result_df_dx2);
	}

	@CodeReflection
	public static double f(final double x, final double y) {
		return x * (-Math.sin(x * y) + y) * 4.0d;
	}

	static double df_dx(double x, double y) {
		return (-Math.sin(x * y) + y - x * Math.cos(x * y) * y) * 4.0d;
	}
}
