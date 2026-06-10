package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaCharLiteralExpressionNode extends LamaExpressionBaseNode {
	private final long value;
	
	public LamaCharLiteralExpressionNode(char value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public long executeLong(VirtualFrame frame) {
        return value;
    }
}
