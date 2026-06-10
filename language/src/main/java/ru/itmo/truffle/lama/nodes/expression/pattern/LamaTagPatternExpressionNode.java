package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.runtime.LamaArray;
import ru.itmo.truffle.lama.runtime.LamaFunction;
import ru.itmo.truffle.lama.runtime.LamaSExp;
import ru.itmo.truffle.lama.runtime.LamaString;

public class LamaTagPatternExpressionNode extends LamaPatternExpressionNode {
	public enum PatternTag {
        VAL, 
        FUN, 
        STR, 
        ARRAY, 
        SEXP, 
        BOX,
    }
    private final PatternTag expectedTag;

    public LamaTagPatternExpressionNode(PatternTag expectedTag) {
        this.expectedTag = expectedTag;
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (value == null)
            return false;

        return switch (expectedTag) {
            case VAL -> (value instanceof Long || value instanceof Integer);
            case STR -> (value instanceof LamaString);
            case FUN -> (value instanceof LamaFunction);
            case ARRAY -> (value instanceof LamaArray);
            case SEXP -> (value instanceof LamaSExp);
            case BOX -> !(value instanceof Long || value instanceof Integer);
        };
    }
}
