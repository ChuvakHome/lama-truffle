package ru.itmo.truffle.lama.nodes.expression;

import java.util.List;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class LamaSequenceExpressionNode extends LamaExpressionBaseNode {
	@Children
    private final LamaExpressionBaseNode[] expressions;

    public LamaSequenceExpressionNode(LamaExpressionBaseNode[] expressions) {
        this.expressions = expressions;
    }
    
    public LamaSequenceExpressionNode(List<LamaExpressionBaseNode> expressions) {
    	this.expressions = expressions.toArray(LamaExpressionBaseNode[]::new);
    }
    
    public LamaExpressionBaseNode[] getExpressions() {
    	return expressions;
    }
    
    public LamaExpressionBaseNode getExpressions(int i) {
    	return expressions[i];
    }
	
	@Override
	@ExplodeLoop
	public Object executeGeneric(VirtualFrame frame) {
		Object lastResult = null;
		
		for (LamaExpressionBaseNode expr : expressions) {
			lastResult = expr.executeGeneric(frame);
        }
		
        return lastResult;
	}
}
