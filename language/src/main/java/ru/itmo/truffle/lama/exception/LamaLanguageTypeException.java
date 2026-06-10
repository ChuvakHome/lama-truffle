package ru.itmo.truffle.lama.exception;

import com.oracle.truffle.api.CompilerDirectives;

public class LamaLanguageTypeException extends LamaLanguageException {
    @CompilerDirectives.TruffleBoundary
    public LamaLanguageTypeException(String message) {
        super(message);
    }

    @CompilerDirectives.TruffleBoundary
    public LamaLanguageTypeException(String message, Object obj) {
        super(formatMessage(message, obj));
    }

    private static String formatMessage(String message, Object obj) {
        return message + ", type: " + obj.getClass().getName();
    }
}