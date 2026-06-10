package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaNullLiteralExpressionNode extends LamaExpressionBaseNode {
	public Object executeGeneric(VirtualFrame frame) {
		return 0L;
	}
}
