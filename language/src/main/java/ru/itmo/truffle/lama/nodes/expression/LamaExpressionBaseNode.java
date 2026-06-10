package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.nodes.LamaBaseNode;
import ru.itmo.truffle.lama.nodes.LamaTypes;

@TypeSystemReference(LamaTypes.class)
public abstract class LamaExpressionBaseNode extends LamaBaseNode {
	public abstract Object executeGeneric(VirtualFrame frame);
	
	public long executeLong(VirtualFrame frame) {
        return (long) executeGeneric(frame);
    }
}
