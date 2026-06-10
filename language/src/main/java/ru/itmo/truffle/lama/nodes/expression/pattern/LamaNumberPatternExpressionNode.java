package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;

public class LamaNumberPatternExpressionNode extends LamaPatternExpressionNode {
	private final long num;

    public LamaNumberPatternExpressionNode(Long constant) {
        this.num = constant;
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (!(value instanceof Long v)) {
            throw new LamaLanguageTypeException("expected Long for pattern", value);
        }
        
        return check(v);
    }

//    @CompilerDirectives.TruffleBoundary
    public boolean check(Long value) {
        return num == value;
    }
}
