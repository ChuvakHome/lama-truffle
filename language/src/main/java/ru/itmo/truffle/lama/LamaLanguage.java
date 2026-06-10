package ru.itmo.truffle.lama;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.strings.TruffleString;

import ru.itmo.truffle.lama.parser.LamaLanguageSourceParser;
import ru.itmo.truffle.lama.runtime.LamaContext;

@TruffleLanguage.Registration(
	id = LamaLanguage.ID,
	name = LamaLanguage.NAME,
//	contextPolicy = ContextPolicy.SHARED,
	characterMimeTypes = LamaLanguage.MIME_TYPE,
	defaultMimeType = LamaLanguage.MIME_TYPE
)
public class LamaLanguage extends TruffleLanguage<LamaContext> {
	public static final String ID = "lama";
	public static final String NAME = "Lama";
	public static final String MIME_TYPE = "application/x-lama";
	public static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_8;
	
	@Override
	protected LamaContext createContext(Env env) {
		return new LamaContext(this, env);
	}
	
	@Override
	protected CallTarget parse(ParsingRequest request) {
		Source source = request.getSource();
		LamaLanguageSourceParser nodeParser = new LamaLanguageSourceParser(this);
		
		return nodeParser.parseProgram(source).getCallTarget();
	}
	
	public static NodeInfo lookupNodeInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        
        NodeInfo info = clazz.getAnnotation(NodeInfo.class);
        if (info != null) {
            return info;
        } else {
            return lookupNodeInfo(clazz.getSuperclass());
        }
	}
}
