package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "|")
public class LamaOrNode extends LamaBinopNode {
	@Specialization
	public long evaluate(long left, long right) {
		return left != 0 
				? 1L 
				: right != 0 ? 1L : 0L
				;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return null;
	}
}
