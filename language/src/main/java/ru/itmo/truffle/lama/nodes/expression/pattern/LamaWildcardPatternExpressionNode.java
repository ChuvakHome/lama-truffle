package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaWildcardPatternExpressionNode extends LamaPatternExpressionNode {
	@Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        return true;
    }
}
