package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class LamaGetClosureExpressionNode extends LamaExpressionBaseNode {
	private final int depth;
	private final int slot;

	public LamaGetClosureExpressionNode(int depth, int slot) {
		this.depth = depth;
		this.slot = slot;
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
		MaterializedFrame current = (MaterializedFrame) frame.getArguments()[0];
		
        for (int i = 1; i < depth; i++) {
            current = (MaterializedFrame) current.getArguments()[0];
        }
        
        return current.getValue(slot);
	}
}
