package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;
import ru.itmo.truffle.lama.runtime.LamaArray;

public class LamaArrayPatternExpressionNode extends LamaPatternExpressionNode {
	@Children
	@CompilationFinal(dimensions = 1)
    private final LamaPatternExpressionNode[] elementPatterns;

    public LamaArrayPatternExpressionNode(LamaPatternExpressionNode[] elementPatterns) {
        this.elementPatterns = elementPatterns;
    }

    @Override
    @ExplodeLoop
    public boolean executeMatch(VirtualFrame frame, Object value) {
        if (!(value instanceof LamaArray arr)) {
            throw new LamaLanguageTypeException("Pattern expects Array from value", value);
        }
        
        if (!check(arr)) {
            return false;
        }
        
        for (int i = 0; i < elementPatterns.length; i++) {
            if (!elementPatterns[i].executeMatch(frame, arr.getByIndex(i))) {
                return false;
            }
        }
        
        return true;
    }

    @CompilerDirectives.TruffleBoundary
    public boolean check(LamaArray arr) {
        return arr.length() == elementPatterns.length;
    }
}
