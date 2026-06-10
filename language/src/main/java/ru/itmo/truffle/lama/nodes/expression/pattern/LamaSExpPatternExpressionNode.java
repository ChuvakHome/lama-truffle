package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ru.itmo.truffle.lama.runtime.LamaSExp;

public class LamaSExpPatternExpressionNode extends LamaPatternExpressionNode {
	private final String expectedTag;
    @Children
    @CompilationFinal(dimensions = 1)
    private final LamaPatternExpressionNode[] args;

    public LamaSExpPatternExpressionNode(String expectedTag, LamaPatternExpressionNode[] args) {
        this.expectedTag = expectedTag;
        this.args = args;
    }

    @Override
    @ExplodeLoop
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (!(value instanceof LamaSExp sexpr)) {
        	return false;
        }

        if (!check(sexpr)) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (!args[i].executeMatch(frame, sexpr.getByIndex(i)))
                return false;
        }
        
        return true;
    }

    @CompilerDirectives.TruffleBoundary
    public boolean check(LamaSExp sexpr) {
        if (!expectedTag.equals(sexpr.getTag()))
            return false;
        
        return args.length == sexpr.getLength();
    }
}
