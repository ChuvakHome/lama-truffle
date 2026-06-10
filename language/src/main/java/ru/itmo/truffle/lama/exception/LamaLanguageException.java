package ru.itmo.truffle.lama.exception;

import java.io.Serial;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.nodes.Node;

public class LamaLanguageException extends AbstractTruffleException {
	@Serial
    private static final long serialVersionUID = 1L;
	
	@CompilerDirectives.TruffleBoundary
    public LamaLanguageException(String message) {
        super(message);
    }

    @CompilerDirectives.TruffleBoundary
    public LamaLanguageException(String message, Node location) {
        super(message, location);
    }

    @CompilerDirectives.TruffleBoundary
    public LamaLanguageException(String message, Throwable cause, Node location) {
        super(message, cause, UNLIMITED_STACK_TRACE, location);
    }    
}
