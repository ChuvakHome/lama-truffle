package ru.itmo.truffle.lama.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.nodes.Node;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.nodes.LamaFunctionNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaLengthBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaReadBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaStringBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaWriteBuiltinRootNode;

public class LamaContext {
	@CompilationFinal 
	private final Env env;
	private final LamaLanguage lang;
	
	public final BufferedReader inReader;
	public final PrintStream out, err;
	
	private final Map<String, Object> globals;
	
	private static final ContextReference<LamaContext> REF = ContextReference.create(LamaLanguage.class);
	
	public LamaContext(LamaLanguage lang, Env env) {
		this.lang = lang;
		this.env = env;
		
		inReader = new BufferedReader(new InputStreamReader(env.in()));
		out = new PrintStream(env.out());
		err = new PrintStream(env.err());
		
		globals = new HashMap<String, Object>();
		installBuiltins();
	}
	
	private void installBuiltins() {
		setGlobal("string", new LamaFunctionNode(
				new LamaStringBuiltinRootNode(lang).getCallTarget(), 1
			));
		setGlobal("length", new LamaFunctionNode(
				new LamaLengthBuiltinRootNode(lang).getCallTarget(), 1
			));
		setGlobal("read", new LamaFunctionNode(
				new LamaReadBuiltinRootNode(lang).getCallTarget(), 0
			));
		setGlobal("write", new LamaFunctionNode(
					new LamaWriteBuiltinRootNode(lang).getCallTarget(), 1
				));
	}
	
	@CompilerDirectives.TruffleBoundary
    public Object getGlobal(String name) {
        return globals.get(name);
    }
	
    public void setGlobal(String name, Object value) {
        globals.put(name, value);
    }
	
	public static LamaContext get(Node node) {
        return REF.get(node);
    }
}
