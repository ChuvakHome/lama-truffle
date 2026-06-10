package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaAliasPatternExpressionNode extends LamaPatternExpressionNode {
	private final int slot;
    @Child
    private LamaPatternExpressionNode innerPattern;

    public LamaAliasPatternExpressionNode(int slot, LamaPatternExpressionNode innerPattern) {
        this.slot = slot;
        this.innerPattern = innerPattern;
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (innerPattern.executeMatch(frame, value)) {
            frame.setObject(slot, value);
            
            return true;
        }
        
        return false;
    }
}
