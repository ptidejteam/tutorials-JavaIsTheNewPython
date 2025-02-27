package net.ptidej.babylon.a.traversal;

import java.lang.reflect.Method;

import jdk.incubator.code.CodeElement;
import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.op.CoreOp;

public class Main {
	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method subMethod = Main.class.getDeclaredMethod("sub", double.class, double.class);
		System.out.println(subMethod);

		final CoreOp.FuncOp funcOp = Op.ofMethod(subMethod).get();
		System.out.println(funcOp.toText());

		// Depth-first search, reporting elements in pre-order
		funcOp.traverse(null, (acc, codeElement) -> {
			// Count the depth of the code element by
			// traversing up the tree from child to parent
			int depth = 0;
			CodeElement<?, ?> parent = codeElement;
			while ((parent = parent.parent()) != null) {
				depth++;
			}
			// Print out code element class
			System.out.println("  ".repeat(depth) + codeElement.getClass());
			return acc;
		});
	}

	@CodeReflection
	public static double sub(final double a, final double b) {
		return a - b;
	}
}
