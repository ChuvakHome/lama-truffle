//package ru.itmo.truffle.lama.nodes;
//
//import com.oracle.truffle.api.frame.FrameDescriptor;
//import com.oracle.truffle.api.frame.VirtualFrame;
//import com.oracle.truffle.api.source.SourceSection;
//import com.oracle.truffle.api.strings.TruffleString;
//
//import ru.itmo.truffle.lama.LamaLanguage;
//import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
//
//public class LamaAstRootNode extends LamaRootNode {
//	@Child private LamaExpressionBaseNode bodyNode;
//
//    private final SourceSection sourceSection;
//    private final TruffleString name;
//    
//	public LamaAstRootNode(
//		LamaLanguage language, 
//		FrameDescriptor frameDescriptor, 
//		LamaExpressionBaseNode bodyNode,
//		SourceSection sourceSection,
//		TruffleString name
//	) {
//		super(language, frameDescriptor);
//		this.bodyNode = bodyNode;
//		this.sourceSection = sourceSection;
//		this.name = name;
//	}
//
//	@Override
//	public SourceSection getSourceSection() {
//		return sourceSection;
//	}
//
//	@Override
//	public LamaExpressionBaseNode getBodyNode() {
//		return bodyNode;
//	}
//
//	@Override
//	public TruffleString getTSName() {
//		return name;
//	}
//
//	@Override
//	public Object execute(VirtualFrame frame) {
//		return bodyNode.executeGeneric(frame);
//	}
//}
