package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaIndexableObject;

public class LamaSetArrayElementExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode arrayExprNode;
    @Child
    private LamaExpressionBaseNode indexExprNode;
    @Child
    private LamaExpressionBaseNode newValueExprNode;
    
    public LamaSetArrayElementExpressionNode(
    			LamaExpressionBaseNode arrayExprNode,
    			LamaExpressionBaseNode indexExprNode,
    			LamaExpressionBaseNode newValueExprNode
    		) {
    	this.arrayExprNode = arrayExprNode;
    	this.indexExprNode = indexExprNode;
    	this.newValueExprNode = newValueExprNode;
    }
    
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object arrayExpr = arrayExprNode.executeGeneric(frame);
		Object indexExpr = indexExprNode.executeGeneric(frame);
		Object valueExpr = newValueExprNode.executeGeneric(frame);
		
		if (!(indexExpr instanceof Long index)) {
			throw new RuntimeException("Index should be a number");
		}
		
		if (arrayExpr instanceof LamaIndexableObject indexableObj) {
			indexableObj.setByIndex(index.intValue(), valueExpr);
			
			return valueExpr;
		}
		
		throw new RuntimeException("Non-indexable expression");
	}
}
