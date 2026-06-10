package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaGetLocalVariableExpressionNode extends LamaExpressionBaseNode {
	private final int slot;

    public LamaGetLocalVariableExpressionNode(int slot) {
        this.slot = slot;
    }
	
    public int getSlot() {
    	return slot;
    }
    
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return frame.getObject(slot);
	}
}
