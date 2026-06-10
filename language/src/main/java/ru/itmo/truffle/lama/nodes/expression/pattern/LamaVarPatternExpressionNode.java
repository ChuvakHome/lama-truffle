package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaVarPatternExpressionNode extends LamaPatternExpressionNode {
	private final int slot;
	
    public LamaVarPatternExpressionNode(int slot) {
    	this.slot = slot;
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        frame.setObject(slot, value);
        
        return true;
    }
}
