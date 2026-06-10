package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;

public abstract class LamaPatternExpressionNode extends LamaExpressionBaseNode {
	public abstract boolean executeMatch(VirtualFrame frame, Object value);

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        throw new UnsupportedOperationException("Patterns cannot be evaluated as normal expressions");
    }
}
