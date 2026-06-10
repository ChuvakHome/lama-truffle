package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class LamaSetClosureExpressionNode extends LamaExpressionBaseNode {
	private final int depth;
	private final int slot;
	@Child
	private LamaExpressionBaseNode valueNode;

	public LamaSetClosureExpressionNode(int depth, int slot, LamaExpressionBaseNode valueNode) {
		this.depth = depth;
		this.slot = slot;
		this.valueNode = valueNode;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getSlot() {
		return slot;
	}
	
	@Override
	@ExplodeLoop
	public Object executeGeneric(VirtualFrame frame) {
		Object value = valueNode.executeGeneric(frame);
		MaterializedFrame current = (MaterializedFrame) frame.getArguments()[0];
		
        for (int i = 1; i < depth; i++) {
            current = (MaterializedFrame) current.getArguments()[0];
        }
        
        current.setObject(slot, value);
        
        return value;
	}
}
