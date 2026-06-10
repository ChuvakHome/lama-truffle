package ru.itmo.truffle.lama.util;

import ru.itmo.truffle.lama.runtime.LamaStringifiable;

public class Utils {
	public static String stringifyLamaObject(Object o) {
		if (o instanceof LamaStringifiable t) {
			return t.toLamaString();
		}
			
		return o.toString();
	}
	
	public static<T> T getOrThrow(boolean condition, T value, Throwable t) {
		if (!condition) {
			throw new RuntimeException(t);
		}
		
		return value;
	}
}
