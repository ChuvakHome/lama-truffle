package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaContext;

public class LamaGetGlobalVariableExpressionNode extends LamaExpressionBaseNode {
	private final String name;

    public LamaGetGlobalVariableExpressionNode(String name) {
        this.name = name;
    }
    
    public String getName() {
    	return name;
    }
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return LamaContext.get(this).getGlobal(name);
	}

}
