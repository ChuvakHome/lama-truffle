package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaString;

public class LamaStringLiteralExpressionNode extends LamaExpressionBaseNode {
	private final LamaString value;

    public LamaStringLiteralExpressionNode(String value) {
        this.value = new LamaString(value);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }
}
