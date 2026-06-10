package ru.itmo.truffle.lama.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;

@ExportLibrary(InteropLibrary.class)
public class LamaString implements TruffleObject, LamaIndexableObject, LamaStringifiable {
	private String str;

    public LamaString(String value) {
        this.str = value;
    }

    @CompilerDirectives.TruffleBoundary
    public int length() {
        return str.length();
    }
    
    public void setByIndex(int index, Object value) {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(index, (char) ((Number) value).longValue());
        str = sb.toString();
    }

    @Override
    @CompilerDirectives.TruffleBoundary
    public Long getByIndex(int index) {
        return (long) str.charAt(index);
    }
    
    @Override
    @CompilerDirectives.TruffleBoundary
    public int getLength() {
    	return str.length();
    }

    public String toLamaString() {
    	return String.format("\"%s\"", str);
    }
    
    public String toString() {
        return this.str;
    }
}
