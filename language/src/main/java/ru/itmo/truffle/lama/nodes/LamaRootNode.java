package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;

public final class LamaRootNode extends RootNode {
	@Child
	private LamaExpressionBaseNode bodyNode;

    public LamaRootNode(LamaLanguage language, FrameDescriptor frameDescriptor, LamaExpressionBaseNode bodyNode) {
        super(language, frameDescriptor);
        
        this.bodyNode = bodyNode;
    }

	@Override
	public Object execute(VirtualFrame frame) {
		return bodyNode.executeGeneric(frame);
	}
}
