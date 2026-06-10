package ru.itmo.truffle.lama.runtime;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;

import ru.itmo.truffle.lama.util.Utils;

@ExportLibrary(InteropLibrary.class)
public class LamaSExp implements TruffleObject, LamaIndexableObject, LamaStringifiable {
	private String tag;
	private Object[] arguments;
	
	public LamaSExp(String tag, Object[] arguments) {
		this.tag = tag;
		this.arguments = arguments;
	}
	
	public void setByIndex(int index, Object value) {
        arguments[index] = value;
    }
	
	@CompilerDirectives.TruffleBoundary
    public Object getByIndex(int index) {
        return arguments[index];
    }
	
	@CompilerDirectives.TruffleBoundary
    public int getLength() {
    	return arguments.length;
    }
	
	public String getTag() {
		return tag;
	}
	
//	public int getArgumentsNumber() {
//		return arguments.length;
//	}
	
	@Override
    @CompilerDirectives.TruffleBoundary
    public String toLamaString() {
        if (arguments.length == 0) {
            return tag;
        } else {
            return Arrays.stream(arguments)
                    .map(Utils::stringifyLamaObject)
                    .collect(Collectors.joining(", ", tag + "( ", ")"));
        }
    }
	
    @Override
    @CompilerDirectives.TruffleBoundary
    public String toString() {
        if (arguments.length == 0) {
            return tag;
        } else {
            return Arrays.stream(arguments)
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", tag + "( ", ")"));
        }
    }
}
