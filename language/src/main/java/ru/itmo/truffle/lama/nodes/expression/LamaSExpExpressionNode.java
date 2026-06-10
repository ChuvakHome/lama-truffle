package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ru.itmo.truffle.lama.runtime.LamaSExp;

public class LamaSExpExpressionNode extends LamaExpressionBaseNode {
	private final String constructor;
    @Children
    private final LamaExpressionBaseNode[] args;

    public LamaSExpExpressionNode(String constructor, LamaExpressionBaseNode[] args) {
        this.constructor = constructor;
        this.args = args;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        Object[] evaluatedArgs = new Object[args.length];
        
        for (int i = 0; i < args.length; i++) {
            evaluatedArgs[i] = args[i].executeGeneric(frame);
        }
        
        return new LamaSExp(constructor, evaluatedArgs);
    }
}
