package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "/")
public class LamaDivNode extends LamaBinopNode {
	@Specialization
	public long evaluate(long left, long right) {
		return left / right;
	}
	
	@Fallback
	public Object typeError(Object left, Object right) {
		System.err.println("Error: " + left + ", " + right);
		throw new RuntimeException("unreachable");
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return null;
	}
}
