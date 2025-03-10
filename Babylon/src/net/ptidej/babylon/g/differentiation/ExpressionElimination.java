// From https://github.com/openjdk/babylon/tree/code-reflection/test/jdk/java/lang/reflect/code/ad

package net.ptidej.babylon.g.differentiation;

import static jdk.incubator.code.analysis.Patterns._P;
import static jdk.incubator.code.analysis.Patterns.constantP;
import static jdk.incubator.code.analysis.Patterns.matchUnusedPureOps;
import static jdk.incubator.code.analysis.Patterns.multiMatch;
import static jdk.incubator.code.analysis.Patterns.opP;
import static jdk.incubator.code.analysis.Patterns.valueP;
import static jdk.incubator.code.op.CoreOp.sub;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
import jdk.incubator.code.Block;
import jdk.incubator.code.CopyContext;
import jdk.incubator.code.Op;
import jdk.incubator.code.Value;
import jdk.incubator.code.analysis.Patterns.OpPattern;
import jdk.incubator.code.analysis.Patterns.Pattern;
import jdk.incubator.code.op.CoreOp;
import jdk.incubator.code.type.JavaType;

public final class ExpressionElimination {
	private ExpressionElimination() {
	}

	static final JavaType J_L_MATH = JavaType.type(Math.class);

	static OpPattern negP(Pattern operand) {
		return opP(CoreOp.NegOp.class, operand);
	}

	static OpPattern addP(Pattern lhs, Pattern rhs) {
		return opP(CoreOp.AddOp.class, lhs, rhs);
	}

	static OpPattern mulP(Pattern lhs, Pattern rhs) {
		return opP(CoreOp.MulOp.class, lhs, rhs);
	}

	public static <T extends Op> T eliminate(T f) {
		// Note expression elimination and other forms of analysis is simplified if first of all expressions
		// are normalized e.g. when they have an operand that is a constant expression
		// and the operation is associative such as add(0, x) -> add(x, 0)

		var actions = multiMatch(new HashMap<Op.Result, BiConsumer<Block.Builder, Op>>(), f)
				.pattern(mulP(_P(), valueP(constantP(0.0d)))).pattern(mulP(valueP(constantP(0.0d)), _P()))
				.pattern(addP(valueP(), constantP(0.0d))).pattern(addP(constantP(0.0d), valueP()))
				.pattern(mulP(constantP(1.0d), valueP())).pattern(mulP(valueP(), constantP(1.0d))).target((ms, as) -> {
					Value a = ms.matchedOperands().get(0);
					as.put(ms.op().result(), (block, op) -> {
						CopyContext cc = block.context();
						cc.mapValue(ms.op().result(), cc.getValue(a));
					});
					return as;
				})
				// add(neg(x), y) -> sub(y, x)
				.pattern(addP(negP(valueP()), valueP())).target((ms, as) -> {
					Value x = ms.matchedOperands().get(0);
					Value y = ms.matchedOperands().get(1);

					as.put(ms.op().result(), (block, op) -> {
						CopyContext cc = block.context();
						Op.Result r = block.op(sub(cc.getValue(y), cc.getValue(x)));
						cc.mapValue(ms.op().result(), r);
					});
					return as;
				}).matchThenApply();

		// Eliminate
		Op ef = f.transform(CopyContext.create(), (block, op) -> {
			BiConsumer<Block.Builder, Op> a = actions.get(op.result());
			if (a != null) {
				a.accept(block, op);
			}
			else {
				block.op(op);
			}
			return block;
		});

		Predicate<Op> testPure = op -> {
			if (op instanceof Op.Pure) {
				return true;
			}
			else {
				return op instanceof CoreOp.InvokeOp c && c.invokeDescriptor().refType().equals(J_L_MATH);
			}
		};

		while (true) {
			Set<Op> unused = matchUnusedPureOps(ef, testPure);
			if (unused.isEmpty()) {
				break;
			}
			// Remove unused ops
			ef = ef.transform(CopyContext.create(), (block, op) -> {
				if (!unused.contains(op)) {
					block.op(op);
				}
				return block;
			});
		}

		@SuppressWarnings("unchecked")
		T t = (T) ef;
		return t;
	}
}