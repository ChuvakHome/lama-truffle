package ru.itmo.truffle.lama.nodes.expression;

import java.util.List;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;

import ru.itmo.truffle.lama.runtime.LamaFunction;

public class LamaCallExpressionNode extends LamaExpressionBaseNode {
	@Child
    private LamaExpressionBaseNode functionNode;
    @Children
    private final LamaExpressionBaseNode[] argumentNodes;
    @Child
    private IndirectCallNode callNode;

    public LamaCallExpressionNode(LamaExpressionBaseNode functionNode, List<LamaExpressionBaseNode> args) {
        this.functionNode = functionNode;
        this.argumentNodes = args.toArray(new LamaExpressionBaseNode[0]);
        this.callNode = Truffle.getRuntime().createIndirectCallNode();
    }

	@Override
	@ExplodeLoop
	public Object executeGeneric(VirtualFrame frame) {
		Object function = functionNode.executeGeneric(frame);
        if (!(function instanceof LamaFunction lamaFunc)) {
            throw new RuntimeException("Object not a function");
        }
        
        Object[] arguments = new Object[argumentNodes.length + 1];
        arguments[0] = lamaFunc.getFrame();
        
        for (int i = 0; i < argumentNodes.length; i++) {
            arguments[i + 1] = argumentNodes[i].executeGeneric(frame);
        }
        
        return callNode.call(lamaFunc.getRootCallTarget(), arguments);
	}
}
