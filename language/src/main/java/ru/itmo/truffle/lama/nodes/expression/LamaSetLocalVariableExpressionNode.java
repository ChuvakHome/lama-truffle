package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaSetLocalVariableExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode valueNode;
	private final int slot;

    public LamaSetLocalVariableExpressionNode(int slot, LamaExpressionBaseNode valueNode) {
        this.slot = slot;
        this.valueNode = valueNode;
    }
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object value = valueNode.executeGeneric(frame);
		
		frame.setObject(slot, value);
		
		return value;
	}

}
