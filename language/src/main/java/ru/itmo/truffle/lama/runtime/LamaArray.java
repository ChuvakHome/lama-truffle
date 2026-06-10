package ru.itmo.truffle.lama.runtime;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;

import ru.itmo.truffle.lama.util.Utils;

@ExportLibrary(InteropLibrary.class)
public class LamaArray implements TruffleObject, LamaIndexableObject, LamaStringifiable {
	private final Object[] elements;

    public LamaArray(int size) {
        this.elements = new Object[size];
    }

    public LamaArray(Object[] elements) {
        this.elements = elements;
    }

    @CompilerDirectives.TruffleBoundary
    public int length() {
        return elements.length;
    }

    @CompilerDirectives.TruffleBoundary
    public Object getByIndex(int index) {
        return elements[index];
    }
    
    @CompilerDirectives.TruffleBoundary
    public int getLength() {
    	return elements.length;
    }
    
    public void setByIndex(int index, Object value) {
        elements[index] = value;
    }
    
    public Object[] getElements() {
        return elements;
    }
    
    @CompilerDirectives.TruffleBoundary
    public String toLamaString() {
    	return Stream.of(elements)
    				.map(Utils::stringifyLamaObject)
    				.collect(Collectors.joining(",", "[", "]"));
    }
    
    @CompilerDirectives.TruffleBoundary 
    public String toString() {
    	return Arrays.deepToString(elements);
    }
}
