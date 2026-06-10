package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;

public class LamaCaseExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode scrutinee;
    @Children
//    @CompilationFinal(dimensions = 1)
    private final LamaCaseBranchNode[] branches;

    public LamaCaseExpressionNode(LamaExpressionBaseNode scrutinee, LamaCaseBranchNode[] branches) {
        this.scrutinee = scrutinee;
        this.branches = branches;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        Object value = scrutinee.executeGeneric(frame);

        for (int i = 0; i < branches.length; i++) {
            LamaCaseBranchNode branch = branches[i];
            
            if (branch.executeMatch(frame, value)) {
                return branch.executeBody(frame);
            }
        }
        
        return 0L;
    }
}
