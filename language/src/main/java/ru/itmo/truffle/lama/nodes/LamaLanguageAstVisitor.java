//package ru.itmo.truffle.lama.nodes;
//
//import org.antlr.v4.runtime.tree.ErrorNode;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.RuleNode;
//import org.antlr.v4.runtime.tree.TerminalNode;
//
//import ru.itmo.truffle.lama.nodes.builtin.LamaReadBuiltinNodeFactory;
//import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.AtomContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.Builtin_stmtContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.ExprContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.FactorContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.ProgramContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.Read_stmtContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.StatementContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.TermContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageParser.Write_stmtContext;
//import ru.itmo.truffle.lama.parser.LamaLanguageVisitor;
//
//public class LamaLanguageAstVisitor implements LamaLanguageVisitor<LamaBaseNode> {
//	@Override
//	public LamaBaseNode visit(ParseTree arg0) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitChildren(RuleNode arg0) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitErrorNode(ErrorNode arg0) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitTerminal(TerminalNode arg0) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitProgram(ProgramContext ctx) {
//		return ctx.statement().getFirst().accept(this);
//	}
//
//	@Override
//	public LamaBaseNode visitStatement(StatementContext ctx) {
//		return ctx.builtin_stmt().accept(this);
//	}
//
//	@Override
//	public LamaBaseNode visitBuiltin_stmt(Builtin_stmtContext ctx) {
//		return ctx.children.getFirst().accept(this);
//	}
//
//	@Override
//	public LamaBaseNode visitRead_stmt(Read_stmtContext ctx) {
//		LamaReadBuiltinNodeFactory.getInstance().createNode();	
//	
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitWrite_stmt(Write_stmtContext ctx) {
//		var expr = (LamaExpressionBaseNode) ctx.expr().accept(this);
//		
//		
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitExpr(ExprContext ctx) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitTerm(TermContext ctx) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitFactor(FactorContext ctx) {
//		return null;
//	}
//
//	@Override
//	public LamaBaseNode visitAtom(AtomContext ctx) {
//		return null;
//	}
//}
