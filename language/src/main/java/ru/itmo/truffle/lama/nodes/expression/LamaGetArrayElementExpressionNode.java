package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaIndexableObject;

public class LamaGetArrayElementExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode arrayExprNode;
    @Child
    private LamaExpressionBaseNode indexExprNode;
    
    public LamaGetArrayElementExpressionNode(LamaExpressionBaseNode arrayExprNode, LamaExpressionBaseNode indexExprNode) {
    	this.arrayExprNode = arrayExprNode;
    	this.indexExprNode = indexExprNode;
    }
    
    public LamaExpressionBaseNode getArrayExprNode() {
    	return arrayExprNode;
    }
    
    public LamaExpressionBaseNode getIndexExprNode() {
    	return indexExprNode;
    }
    
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object arrayExpr = arrayExprNode.executeGeneric(frame);
		Object indexExpr = indexExprNode.executeGeneric(frame);
		
		if (!(indexExpr instanceof Long index)) {
			throw new RuntimeException("Index should be a number");
		}
		
		if (arrayExpr instanceof LamaIndexableObject indexableObj) {
			return indexableObj.getByIndex(index.intValue());
		}
		
		throw new RuntimeException("Non-indexable expression");
	}
}
