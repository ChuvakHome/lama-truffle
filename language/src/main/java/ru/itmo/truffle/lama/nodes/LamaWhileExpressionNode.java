package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;

public class LamaWhileExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode condition;
    @Child
    private LamaExpressionBaseNode body;
    
    public LamaWhileExpressionNode(LamaExpressionBaseNode condition, LamaExpressionBaseNode body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        while (checkCondition(frame)) {
            body.executeGeneric(frame);
        }

        return 0L;
    }

    private boolean checkCondition(VirtualFrame frame) {
        long value = condition.executeLong(frame);
        
		return value != 0;
    }
}
