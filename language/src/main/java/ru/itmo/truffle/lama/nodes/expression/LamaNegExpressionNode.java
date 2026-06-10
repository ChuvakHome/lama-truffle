package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "-")
public class LamaNegExpressionNode extends LamaExpressionBaseNode {
	@Child 
	private LamaExpressionBaseNode valueNode;

    public LamaNegExpressionNode(LamaExpressionBaseNode valueNode) {
        this.valueNode = valueNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return executeLong(frame);
    }

    @Override
    public long executeLong(VirtualFrame frame) {
        return -valueNode.executeLong(frame);
    }
}
