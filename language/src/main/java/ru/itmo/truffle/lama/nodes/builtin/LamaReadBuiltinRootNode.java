package ru.itmo.truffle.lama.nodes.builtin;

import java.io.IOException;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.exception.LamaLanguageException;
import ru.itmo.truffle.lama.runtime.LamaContext;

public class LamaReadBuiltinRootNode extends RootNode {
	public LamaReadBuiltinRootNode(LamaLanguage language) {
        super(language);
    }
	
	@Override
	public Object execute(VirtualFrame frame) {
		String s;
		try {
			LamaContext.get(this).out.print("> ");
			s = LamaContext.get(this).inReader.readLine().strip();
			
			return Long.parseLong(s.strip());
		} catch (IOException e) {
			throw new LamaLanguageException("Error while reading stdin due to " + e.getMessage(), this);
		}
	}
}
