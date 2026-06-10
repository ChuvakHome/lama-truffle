package ru.itmo.truffle.lama.nodes.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.exception.LamaLanguageException;
import ru.itmo.truffle.lama.runtime.LamaContext;

public final class LamaWriteBuiltinRootNode extends RootNode {
	public LamaWriteBuiltinRootNode(LamaLanguage language) {
        super(language);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        
        if (args.length != 2) {
            throw new LamaLanguageException("expected only two arguments for write", this);
        }
        
        Object arg = args[1];
        LamaContext.get(this).out.println(arg.toString());
        
        return 0;
    }
}
