package ru.itmo.truffle.lama.nodes.expression.pattern;

import com.oracle.truffle.api.frame.VirtualFrame;

import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;

public class LamaBoolConstPatternExpressionNode extends LamaPatternExpressionNode {
	private boolean value;
	
	public LamaBoolConstPatternExpressionNode(boolean value) {
		this.value = value;
	}
	
	@Override
	public boolean executeMatch(VirtualFrame frame, Object value) {
		if (value instanceof Boolean b) {
			return this.value == b.booleanValue();
		} else if (value instanceof Long l) {
			return this.value && (l.longValue() != 0L) || !this.value && (l.longValue() == 0L);  
		}
		
		throw new LamaLanguageTypeException("expected boolean or long value, got " + value);
	}

}
