package net.ptidej.babylon.f.replace;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.CopyContext;
import jdk.incubator.code.Op;
import jdk.incubator.code.interpreter.Interpreter;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method subMethod = Main.class.getDeclaredMethod("sub", double.class, double.class);
		System.out.println(subMethod);

		final CoreOp.FuncOp funcOp1 = Op.ofMethod(subMethod).get();
		System.out.println(funcOp1.toText());
		final Double resultsInterpreted1 = (Double) Interpreter.invoke(MethodHandles.lookup(), funcOp1, 32, 10);
		System.out.println(resultsInterpreted1);

		final CoreOp.FuncOp funcOp2 = funcOp1.transform((builder, op) -> {
			final CopyContext cc = builder.context();
			if (op instanceof CoreOp.SubOp subOp) {
				final Op.Result inputResult = subOp.result();
				final Op.Result lhs = (Op.Result) cc.getProperty("beforeLast");
				final Op.Result rhs = (Op.Result) cc.getProperty("last");
				final Op.Result outputResult = builder.op(CoreOp.add(lhs, rhs));
				cc.mapValue(inputResult, outputResult);
			}
			else if (op instanceof CoreOp.VarAccessOp.VarLoadOp varLoadOp) {
				final Op.Result result = builder.op(varLoadOp);
				cc.putProperty("beforeLast", cc.getProperty("last"));
				cc.putProperty("last", result);
			}
			else {
				builder.op(op);
			}
			return builder;
		});
		System.out.println(funcOp2.toText());
		final Double resultsInterpreted2 = (Double) Interpreter.invoke(MethodHandles.lookup(), funcOp2, 32, 10);
		System.out.println(resultsInterpreted2);
	}

	@CodeReflection
	public static double sub(final double a, final double b) {
		return a - b;
	}
}
