package net.ptidej.babylon.d.graphs;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jdk.incubator.code.Block;
import jdk.incubator.code.CodeItem;
import jdk.incubator.code.CodeReflection;
import jdk.incubator.code.Op;
import jdk.incubator.code.Value;
import jdk.incubator.code.op.CoreOp;
import jdk.incubator.code.writer.OpWriter;

public class Main {
	@CodeReflection
	public static double distance(double a, double b) {
		return Math.abs(a - b);
	}

	public static void main(final String[] args) throws NoSuchMethodException, SecurityException {
		final Method distanceMethod = Main.class.getDeclaredMethod("distance", double.class, double.class);
		System.out.println(distanceMethod);

		final CoreOp.FuncOp distanceFuncOp = Op.ofMethod(distanceMethod).get();
		System.out.println(distanceFuncOp.toText());

		Function<CodeItem, String> names = Main.names(distanceFuncOp);

		// Create the expression graph for the terminating operation result
		final Op.Result returnResult = distanceFuncOp.body().entryBlock().terminatingOp().result();
		final Node<Value> returnGraph = Node.expressionGraph(returnResult);
		// Transform from Node<Value> to Node<String> and print the graph
		System.out.println("ICI!");
		System.out.println(returnGraph.transformValues(v -> Main.printValue(names, v)));

		// Lets compute the two use graphs and print them out.
		for (Block.Parameter parameter : distanceFuncOp.parameters()) {
			Node<Value> useNode = Node.useGraph(parameter);
			System.out.println(useNode.transformValues(v -> printValue(names, v)));
		}
	}

	static Function<CodeItem, String> names(final Op op) {
		final OpWriter w = new OpWriter(Writer.nullWriter(), OpWriter.VoidOpResultOption.WRITE_VOID);
		w.writeOp(op);
		return w.namer();
	}

	static String printOpHeader(final Function<CodeItem, String> names, final Op op) {
		return OpWriter.toText(op, OpWriter.OpDescendantsOption.DROP_DESCENDANTS,
				OpWriter.VoidOpResultOption.WRITE_VOID, OpWriter.CodeItemNamerOption.of(names));
	}

	static String printOpWriteVoid(Function<CodeItem, String> names, Op op) {
		return OpWriter.toText(op, OpWriter.VoidOpResultOption.WRITE_VOID, OpWriter.CodeItemNamerOption.of(names));
	}

	static String printValue(final Function<CodeItem, String> names, final Value v) {
		if (v instanceof Op.Result opr) {
			return Main.printOpHeader(names, opr.op());
		} else {
			return "%" + names.apply(v) + " <block parameter>";
		}
	}
}

record Node<T>(T value, List<Node<T>> edges) {
	static Node<Value> expressionGraph(final Value value) {
		return Node.expressionGraph(new HashMap<>(), value);
	}

	static Node<Value> expressionGraph(final Map<Value, Node<Value>> visited, final Value value) {
		// If value has already been visited return its node
		if (visited.containsKey(value)) {
			return visited.get(value);
		}

		// Find the expression graphs for each operand
		final List<Node<Value>> edges = new ArrayList<>();
		for (final Value operand : value.dependsOn()) {
			edges.add(Node.expressionGraph(operand));
		}
		final Node<Value> node = new Node<>(value, edges);
		visited.put(value, node);
		return node;
	}

	static Node<Value> useGraph(final Value value) {
		return Node.useGraph(new HashMap<>(), value);
	}

	static Node<Value> useGraph(final Map<Value, Node<Value>> visited, final Value value) {
		// If value has already been visited return its node
		if (visited.containsKey(value)) {
			return visited.get(value);
		}

		// Find the use graphs for each use
		final List<Node<Value>> edges = new ArrayList<>();
		for (final Op.Result use : value.uses()) {
			edges.add(Node.useGraph(visited, use));
		}
		final Node<Value> node = new Node<>(value, edges);
		visited.put(value, node);
		return node;
	}

	<U> Node<U> transformValues(final Function<T, U> f) {
		final List<Node<U>> transformedEdges = new ArrayList<>();
		for (final Node<T> edge : edges()) {
			transformedEdges.add(edge.transformValues(f));
		}
		return new Node<>(f.apply(this.value()), transformedEdges);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		print(sb, "", "");
		return sb.toString();
	}

	private void print(StringBuilder sb, String prefix, String edgePrefix) {
		sb.append(prefix);
		sb.append(value);
		sb.append('\n');
		for (final Iterator<Node<T>> it = edges.iterator(); it.hasNext();) {
			final Node<T> edge = it.next();
			if (it.hasNext()) {
				edge.print(sb, edgePrefix + "├── ", edgePrefix + "│   ");
			} else {
				edge.print(sb, edgePrefix + "└── ", edgePrefix + "    ");
			}
		}
	}
}
