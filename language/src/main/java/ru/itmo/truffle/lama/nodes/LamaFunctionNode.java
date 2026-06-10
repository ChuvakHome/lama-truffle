package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
import ru.itmo.truffle.lama.runtime.LamaFunction;

public class LamaFunctionNode extends LamaExpressionBaseNode {
	private final CallTarget target;
    private final int arity;

    public LamaFunctionNode(CallTarget target, int arity) {
        this.target = target;
        this.arity = arity;
    }
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return new LamaFunction(target, arity, frame.materialize());
	}
}
