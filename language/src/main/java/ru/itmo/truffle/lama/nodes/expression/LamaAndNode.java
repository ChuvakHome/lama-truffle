package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "|")
public class LamaAndNode extends LamaBinopNode {
	@Specialization
	public long evaluate(long left, long right) {
		return left != 0 
				? right != 0 ? 1L : 0L
				: 0
				;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return null;
	}
	
}
