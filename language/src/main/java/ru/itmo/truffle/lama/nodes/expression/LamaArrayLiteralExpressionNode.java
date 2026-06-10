package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ru.itmo.truffle.lama.runtime.LamaArray;

public final class LamaArrayLiteralExpressionNode extends LamaExpressionBaseNode {
	@Children
    private final LamaExpressionBaseNode[] arrayElements;

    public LamaArrayLiteralExpressionNode(LamaExpressionBaseNode[] arrayElements) {
        this.arrayElements = arrayElements;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
    	Object[] values = new Object[arrayElements.length];
    	
    	for (int i = 0; i < arrayElements.length; ++i) {
    		values[i] =  arrayElements[i].executeGeneric(frame);
    	}
    	
        return new LamaArray(values);
    }
}
