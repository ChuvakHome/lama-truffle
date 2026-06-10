package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LamaSetArgumentExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode valueNode;
    private final int index;

    public LamaSetArgumentExpressionNode(int index, LamaExpressionBaseNode valueNode) {
        this.index = index;
        this.valueNode = valueNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
    	Object value = valueNode.executeGeneric(frame);
    	Object[] args = frame.getArguments();
    	args[index] = value;
    	
        return value;
    }
}
