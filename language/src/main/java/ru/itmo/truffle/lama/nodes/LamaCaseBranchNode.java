package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaPatternExpressionNode;

public class LamaCaseBranchNode extends Node {
	@Child
    private LamaPatternExpressionNode pattern;
    @Child
    private LamaExpressionBaseNode body;

    public LamaCaseBranchNode(LamaPatternExpressionNode pattern, LamaExpressionBaseNode body) {
        this.pattern = pattern;
        this.body = body;
    }

    public boolean executeMatch(VirtualFrame frame, Object value) {
        return pattern.executeMatch(frame, value);
    }

    public Object executeBody(VirtualFrame frame) {
        return body.executeGeneric(frame);
    }
}
