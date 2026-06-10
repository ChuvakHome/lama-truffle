package ru.itmo.truffle.lama.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class LamaLanguageParseException extends LamaLanguageException {
	public LamaLanguageParseException(String message) {
        super(message);
    }

    public LamaLanguageParseException(String message, ParserRuleContext ctx) {
        super(formatMessage(message, ctx));
    }
    
    public LamaLanguageParseException(String message, Token tok) {
        super(formatMessage(message, tok));
    }

    private static String formatMessage(String message, ParserRuleContext ctx) {
        if (ctx == null)
            return message;
        
        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        
        return String.format("Found %s at line %d:%d", message, line, col);
    }
    
    private static String formatMessage(String message, Token tok) {
        if (tok == null)
            return message;
        
        int line = tok.getLine();
        int col = tok.getCharPositionInLine();
        
        return "Found " + message + " at line " + line + ":" + col;
    }
}
