package ru.itmo.truffle.lama.runtime;

public interface LamaIndexableObject {
	Object getByIndex(int index);
	
	void setByIndex(int index, Object value);
	
	int getLength();
}
