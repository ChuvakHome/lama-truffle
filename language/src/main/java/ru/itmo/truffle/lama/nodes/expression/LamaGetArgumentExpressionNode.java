package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.exception.LamaLanguageException;

public class LamaGetArgumentExpressionNode extends LamaExpressionBaseNode {
	private final int index;

    public LamaGetArgumentExpressionNode(int index) {
        this.index = index;
    }
	
    public int getIndex() {
    	return index;
    }
    
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        
        if (args == null || index < 0 || index >= args.length) {
            throw new LamaLanguageException("Argument index out of bounds: " + index, this);
        }
        
        return args[index];
    }

    @Override
    public long executeLong(VirtualFrame frame) {
        Object value = executeGeneric(frame);
        
        if (value instanceof Long) {
            return (Long) value;
        }
        
        throw new LamaLanguageException("Argument is not long", this);
    }

}
