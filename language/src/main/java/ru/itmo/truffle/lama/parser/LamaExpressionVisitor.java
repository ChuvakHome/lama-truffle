//package ru.itmo.truffle.lama.parser;
//
//import ru.itmo.truffle.lama.nodes.expression.LamaAddNodeGen;
//import ru.itmo.truffle.lama.nodes.expression.LamaDivNodeGen;
//import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
//import ru.itmo.truffle.lama.nodes.expression.LamaIntegerLiteralExpressionNode;
//import ru.itmo.truffle.lama.nodes.expression.LamaMulNodeGen;
//import ru.itmo.truffle.lama.nodes.expression.LamaSubNodeGen;
//
//public class LamaExpressionVisitor extends LamaLanguageBaseVisitor<LamaExpressionBaseNode> {
//	@Override
//	public LamaExpressionBaseNode visitTerm(LamaLanguageParser.TermContext ctx) {
//		if (ctx.term() == null) {
//			return visitFactor(ctx.factor());
//		}
//		
//		LamaExpressionBaseNode left = visitTerm(ctx.term());
//		LamaExpressionBaseNode right = visitFactor(ctx.factor());
//		
//		return ctx.PLUS_OP() != null ? LamaAddNodeGen.create(left, right)
//				: LamaSubNodeGen.create(left, right);
//	}
//	
//	@Override
//	public LamaExpressionBaseNode visitFactor(LamaLanguageParser.FactorContext ctx) {
//		if (ctx.factor() == null) {
//			return visitAtom(ctx.atom());
//		}
//		
//		LamaExpressionBaseNode left = visitFactor(ctx.factor());
//		LamaExpressionBaseNode right = visitAtom(ctx.atom());
//		
//		return ctx.MULTI_OP() != null ? LamaMulNodeGen.create(left, right)
//				: LamaDivNodeGen.create(left, right);
//	}
//	
//	@Override
//	public LamaExpressionBaseNode visitAtom(LamaLanguageParser.AtomContext ctx) {
//		return new LamaIntegerLiteralExpressionNode(Long.parseLong(ctx.NUMBER().getText()));
//	}
//}
