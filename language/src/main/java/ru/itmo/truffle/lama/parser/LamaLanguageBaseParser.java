package ru.itmo.truffle.lama.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.Token;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.strings.TruffleString;

import ru.itmo.truffle.lama.LamaLanguage;

public class LamaLanguageBaseParser extends LamaLanguageBaseVisitor<Void> {
	protected final LamaLanguage lang;
	protected final Source source;
	
	protected LamaLanguageBaseParser(LamaLanguage lang, Source source) {
		this.lang = lang;
		this.source = source;
	}
	
	protected TruffleString asTruffleString(Token literalToken) {
        return TruffleString.fromJavaStringUncached(literalToken.getText(), LamaLanguage.STRING_ENCODING);
    }
	
	private int totalLocals;
	
	private class Scope {
        protected final Scope parent;
        // Maps local names to a unique index.
        private final Map<TruffleString, Integer> locals;
        // Tracks which locals have been initialized in this scope.
//        private final Set<TruffleString> initialized;

        Scope(Scope parent) {
            this.parent = parent;
            locals = new HashMap<>(parent != null ? parent.locals : null);
//            initialized = new HashSet<>(parent.initialized);
        }

        Scope() {
            this.parent = null;
            locals = new HashMap<>();
//            initialized = new HashSet<>();
        }

        boolean localDeclared(TruffleString name) {
            return locals.containsKey(name);
        }

        /**
         * Declares a local with the given name in the current scope. Assigns a unique local index
         * to it.
         */
        void declareLocal(TruffleString name) {
            locals.put(name, totalLocals++);
        }

        /**
         * Returns the unique local index of a name in the current scope. The local index for a
         * given name can change between scopes (e.g., variable "a" will have different indices in
         * two disjoint scopes).
         */
        Integer getLocalIndex(TruffleString name) {
            Integer i = locals.get(name);
            if (i == null) {
                return -1;
            } else {
                return i;
            }
        }

//        /**
//         * Mark the given local as initialized. Maintaining information about initialized locals
//         * allows the AST parser to identify new variable assignments.
//         */
//        boolean initializeLocal(TruffleString name) {
//            return initialized.add(name);
//        }
    }
	
	protected class GlobalScope extends Scope {
		GlobalScope() {
			super(null);
			declareLocal(TruffleString.fromJavaStringUncached("write", LamaLanguage.STRING_ENCODING));
		}
	}
	
//	protected interface Variable {
//		interface Visitor<R> {
//            R visitGlobal(Global global);
//
//            R visitLocal(Local local);
//
//            R visitCapture(Capture capture);
//        }
//	}
}
