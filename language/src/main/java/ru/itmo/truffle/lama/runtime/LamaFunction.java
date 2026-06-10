package ru.itmo.truffle.lama.runtime;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.interop.TruffleObject;

public class LamaFunction implements TruffleObject {
	private final CallTarget callTarget;
	private final int arguments;
	private final MaterializedFrame frame;
	
	public LamaFunction(CallTarget callTarget, int arguments, MaterializedFrame frame) {
		this.callTarget = callTarget;
		this.arguments = arguments;
		this.frame = frame;
	}
	
	public CallTarget getRootCallTarget() {
		return callTarget;
	}
	
	public int getArguments() {
		return arguments;
	}
	
	public MaterializedFrame getFrame() {
		return frame;
	}
}
