package ru.itmo.truffle.lama.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.instrumentation.ProbeNode;

@GenerateWrapper
public abstract class LamaStatementNode extends LamaBaseNode implements InstrumentableNode {
	public abstract void executeVoid(VirtualFrame frame);
	
	@Override 
	public WrapperNode createWrapper(ProbeNode probeNode) {
		return new LamaStatementNodeWrapper(this, probeNode);
    }
}
