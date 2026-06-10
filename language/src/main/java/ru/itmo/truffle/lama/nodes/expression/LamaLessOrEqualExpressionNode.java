package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "<=")
public class LamaLessOrEqualExpressionNode extends LamaExpressionBaseNode {
	@Child 
	private LamaExpressionBaseNode leftNode;
    @Child 
    private LamaExpressionBaseNode rightNode;
    
    public LamaLessOrEqualExpressionNode(LamaExpressionBaseNode leftNode, LamaExpressionBaseNode rightNode) {
    	this.leftNode = leftNode;
    	this.rightNode = rightNode;
    }
    
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return executeLong(frame);
    }

    @Override
    public long executeLong(VirtualFrame frame) {
        long left = leftNode.executeLong(frame);
        long right = rightNode.executeLong(frame);
        
        return left <= right ? 1L : 0L;
    }
}
