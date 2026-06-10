package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaContext;

public class LamaSetGlobalVariableExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode valueNode;
	private final String name;

    public LamaSetGlobalVariableExpressionNode(String name, LamaExpressionBaseNode valueNode) {
        this.name = name;
        this.valueNode = valueNode;
    }
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object value = valueNode.executeGeneric(frame);
		
		LamaContext.get(this).setGlobal(name, value);
		
		return value;
	}

}
