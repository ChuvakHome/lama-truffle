package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;

public class LamaCharPatternExpressionNode extends LamaPatternExpressionNode {
	private final char ch;

    public LamaCharPatternExpressionNode(char ch) {
        this.ch = ch;
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (!(value instanceof Character)) {
            throw new LamaLanguageTypeException("Expected Char for pattern", value);
        }
            
        return ch == (char) value;
    }
}
