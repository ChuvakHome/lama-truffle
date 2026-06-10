package ru.itmo.truffle.lama.nodes.expression;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("left")
@NodeChild("right")
public abstract class LamaBinopNode extends LamaExpressionBaseNode {
//	@Child
//    private LamaExpressionBaseNode left;
//    @Child
//    private LamaExpressionBaseNode right;
}
