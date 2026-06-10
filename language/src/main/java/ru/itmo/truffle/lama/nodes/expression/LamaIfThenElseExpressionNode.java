package ru.itmo.truffle.lama.nodes.expression;

import java.util.List;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public final class LamaIfThenElseExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode conditionNode;
    @Child
    private LamaExpressionBaseNode thenNode;
    @Children
    private final LamaExpressionBaseNode[] elifConditions;
    @Children
    private final LamaExpressionBaseNode[] elifBodies;
    @Child
    private LamaExpressionBaseNode elseNode;

    public LamaIfThenElseExpressionNode(LamaExpressionBaseNode conditionNode, LamaExpressionBaseNode thenNode,
                      List<LamaExpressionBaseNode> elifConditions,
                      List<LamaExpressionBaseNode> elifBodies,
                      LamaExpressionBaseNode elseNode) {
        this.conditionNode = conditionNode;
        this.thenNode = thenNode;
        this.elifConditions = elifConditions.toArray(LamaExpressionBaseNode[]::new);
        this.elifBodies = elifBodies.toArray(LamaExpressionBaseNode[]::new);
        this.elseNode = elseNode;
    }
    
    public LamaExpressionBaseNode getConditionNode() {
    	return conditionNode;
    }
    
    public LamaExpressionBaseNode getThenNode() {
    	return thenNode;
    }
    
    public LamaExpressionBaseNode[] getElifConditions() {
    	return elifConditions;
    }
    
    public LamaExpressionBaseNode[] getElifBodies() {
    	return elifBodies;
    }
    
    public LamaExpressionBaseNode getElseNode() {
    	return elseNode;
    }
    
    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {    	
        if (isTrue(conditionNode.executeGeneric(frame))) {	
            return thenNode.executeGeneric(frame);
        }
        
        for (int i = 0; i < elifConditions.length; i++) {
            if (isTrue(elifConditions[i].executeGeneric(frame))) {
                return elifBodies[i].executeGeneric(frame);
            }
        }
        
        if (elseNode != null) {
            return elseNode.executeGeneric(frame);
        }
        
        return null; // No branch was accessed
    }
    
    @CompilerDirectives.TruffleBoundary
    private static boolean isTrue(Object value) {
        if (value instanceof Long) {
            return (Long) value != 0;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        }
        
        throw new RuntimeException("Condition must be a Long or Boolean");
    }
}
