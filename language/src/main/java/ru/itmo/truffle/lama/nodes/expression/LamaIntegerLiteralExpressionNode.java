package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "integerLiteral")
public final class LamaIntegerLiteralExpressionNode extends LamaExpressionBaseNode {
	private final long value;
	
	public LamaIntegerLiteralExpressionNode(long value) {
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return executeLong(frame);
	}
	
	@Override
	public long executeLong(VirtualFrame frame) {
		return value;
	}
}
