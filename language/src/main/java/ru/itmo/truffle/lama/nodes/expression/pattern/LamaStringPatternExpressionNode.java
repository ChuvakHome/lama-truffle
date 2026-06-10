package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;
import ru.itmo.truffle.lama.runtime.LamaString;

public class LamaStringPatternExpressionNode extends LamaPatternExpressionNode {
	private final LamaString content;

    public LamaStringPatternExpressionNode(String str) {
        this.content = new LamaString(str);
    }

    @Override
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (!(value instanceof LamaString str)) {
            throw new LamaLanguageTypeException("Expected String for pattern", value);
        }
        
        return check(str);
    }

    @CompilerDirectives.TruffleBoundary
    public boolean check(LamaString value) {
        return content.toString().equals(value.toString());
    }
}
