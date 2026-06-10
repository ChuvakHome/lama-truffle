package ru.itmo.truffle.lama.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.exception.LamaLanguageException;
import ru.itmo.truffle.lama.exception.LamaLanguageTypeException;
import ru.itmo.truffle.lama.runtime.LamaIndexableObject;

public class LamaLengthBuiltinRootNode extends RootNode {
	public LamaLengthBuiltinRootNode(LamaLanguage language) {
        super(language);
    }
	
	@Override
	public Object execute(VirtualFrame frame) {
		Object[] args = frame.getArguments();
        
        if (args.length != 2) {
            throw new LamaLanguageException("expected only two arguments for length", this);
        }
        
        Object arg = args[1];
        
        if (arg instanceof LamaIndexableObject indexableObj) {
        	return (long) indexableObj.getLength();
        }
        
        throw new LamaLanguageTypeException("length argument expected to be a string, array or s-exp", arg);
	}
}
