package ru.itmo.truffle.lama.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;

import ru.itmo.truffle.lama.LamaLanguage;
import ru.itmo.truffle.lama.exception.LamaLanguageParseException;
import ru.itmo.truffle.lama.nodes.LamaCaseBranchNode;
import ru.itmo.truffle.lama.nodes.LamaCaseExpressionNode;
import ru.itmo.truffle.lama.nodes.LamaDoWhileExpressionNode;
import ru.itmo.truffle.lama.nodes.LamaFunctionNode;
import ru.itmo.truffle.lama.nodes.LamaRootNode;
import ru.itmo.truffle.lama.nodes.LamaWhileExpressionNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaLengthBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaReadBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaStringBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.builtin.LamaWriteBuiltinRootNode;
import ru.itmo.truffle.lama.nodes.expression.LamaAddNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaAndNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaArrayLiteralExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaCallExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaCharLiteralExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaDivNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaEqualExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaExpressionBaseNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGetArgumentExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGetArrayElementExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGetClosureExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGetGlobalVariableExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGetLocalVariableExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGreaterExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaGreaterOrEqualExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaIfThenElseExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaIntegerLiteralExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaLessExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaLessOrEqualExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaModNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaMulNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaNegExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaNotEqualExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaNullLiteralExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaOrNodeGen;
import ru.itmo.truffle.lama.nodes.expression.LamaSExpExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSequenceExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSetArgumentExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSetArrayElementExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSetClosureExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSetGlobalVariableExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSetLocalVariableExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaStringLiteralExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.LamaSubNodeGen;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaAliasPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaArrayPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaBoolConstPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaCharPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaNumberPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaSExpPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaStringPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaTagPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaTagPatternExpressionNode.PatternTag;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaVarPatternExpressionNode;
import ru.itmo.truffle.lama.nodes.expression.pattern.LamaWildcardPatternExpressionNode;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.AccessExprContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.AdditiveExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.AliasPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ArrayLiteralContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ArrayPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.AssignExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.CallExprContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.CaseExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.CharPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ConsExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ConsPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.DecimalPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.DefinitionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.DoWhileLoopContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.DotExprContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.EqualityExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.FalsePatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ForLoopContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.FunDefinitionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.IdenContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.IfExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.LambdaExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.LetExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ListLiteralContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ListPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.LiteralContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.LogicalAndExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.LogicalOrExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.MultiplicativeExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ParenPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ParenPrimaryContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternArrayTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternBoxTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternFunTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternSexpTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternStrTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PatternValTagContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.PrefixExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.RelationalExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.ScopeExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.SequenceExpressionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.SexpExprContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.SexpPatternNoArgsContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.SexpPatternWithArgsContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.SkipContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.StringPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.TopScopeContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.TruePatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.VarDefinitionContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.VarInitContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.VarPatternContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.WhileLoopContext;
import ru.itmo.truffle.lama.parser.LamaLanguageParser.WildcardPatternContext;
import ru.itmo.truffle.lama.util.Utils;

public class LamaLanguageSourceParser extends LamaLanguageBaseVisitor<LamaExpressionBaseNode> {
	private LamaLanguage lang;
	
	private FrameDescriptor.Builder builder;
	private LamaScope currentScope;
	
	private Set<String> globalVars = new HashSet<String>();
	private final Map<String, LamaExpressionBaseNode> builtinFunctions;
	
	private static class LamaScope {
		private final LamaScope parent;
		private final boolean isFunctionBoundary;
	    private final Map<String, Integer> locals = new HashMap<>();
	    
	    public LamaScope(LamaScope parent, boolean isFunctionBoundary) {
	        this.parent = parent;
	        this.isFunctionBoundary = isFunctionBoundary;
	    }
	    
	    public int[] resolve(String name) {
	        LamaScope curr = this;
	        int depth = 0;
	        while (curr != null) {
	            if (curr.locals.containsKey(name)) {
	                return new int[]{depth, curr.locals.get(name)};
	            }
	            
	            if (curr.isFunctionBoundary) {
	                depth++;
	            }
	            
	            curr = curr.parent;
	        }
	        
	        return null;
	    }
	}
	
	public LamaLanguageSourceParser(LamaLanguage lang) {
		this.lang = lang;
		
		builtinFunctions = Map.of(
					"string", new LamaFunctionNode(new LamaStringBuiltinRootNode(lang).getCallTarget(), 1),
					"length", new LamaFunctionNode(new LamaLengthBuiltinRootNode(lang).getCallTarget(), 1),
					"read", new LamaFunctionNode(new LamaReadBuiltinRootNode(lang).getCallTarget(), 0),
					"write", new LamaFunctionNode(new LamaWriteBuiltinRootNode(lang).getCallTarget(), 1)
				);
	}
	
	private LamaLanguageSourceParser(LamaLanguage lang, FrameDescriptor.Builder builder, LamaScope scope, Set<String> globalNames) {
		this(lang);
		
		this.builder = builder;
		this.currentScope = scope;
		this.globalVars = globalNames;
	}
	
	private void processVarDecl(String name) {
		if (currentScope.locals.containsKey(name)) {
			throw new LamaLanguageParseException("Duplicate variable: " + name);
		}
		
		int slot = builder.addSlot(FrameSlotKind.Object, name, null);
		
		currentScope.locals.put(name, slot);
	}
	
	public LamaRootNode parseProgram(Source source) {
		LamaLanguageLexer lexer = new LamaLanguageLexer(CharStreams.fromString(source.getCharacters().toString()));
		LamaLanguageParser parser = new LamaLanguageParser(new CommonTokenStream(lexer));
		
		globalVars.addAll(builtinFunctions.keySet());
		builder = FrameDescriptor.newBuilder();
		currentScope = new LamaScope(currentScope, false);
		
		List<LamaExpressionBaseNode> topLevelExprs = new ArrayList<>(builtinFunctions.entrySet().stream().map(builtinFunEntry ->
			(LamaExpressionBaseNode) new LamaSetGlobalVariableExpressionNode(builtinFunEntry.getKey(), builtinFunEntry.getValue())
		).toList());
		
		LamaExpressionBaseNode expr = visitTopScope(parser.program().topScope());
		topLevelExprs.add(expr);
		
		return new LamaRootNode(
					lang,
					builder.build(), 
					new LamaSequenceExpressionNode(topLevelExprs.toArray(LamaExpressionBaseNode[]::new))
				);
	}
	
	@Override
	public LamaExpressionBaseNode visitTopScope(TopScopeContext ctx) {
		List<LamaExpressionBaseNode> seqNodes = new ArrayList<>();

		ctx.definition().forEach(defCtx -> {
			if (defCtx.varDefinition() != null) {
				defCtx.varDefinition().varInit().forEach(varInit -> globalVars.add(varInit.LIDENT().getText()));
			} else {
				globalVars.add(
					defCtx.funDefinition().LIDENT().getText()
				);
			}
		});
		
		ctx.definition().forEach(defCtx -> {
			if (defCtx.varDefinition() != null) {
				seqNodes.addAll(
					processTopLevelVarInits(defCtx.varDefinition().varInit())
				);
			} else {
				seqNodes.add(processGlobalFunDef(defCtx.funDefinition()));
			}
		});
		
		LamaExpressionBaseNode exprNode = ctx.expression().accept(this);
		seqNodes.add(exprNode);
		
		return new LamaSequenceExpressionNode(seqNodes.toArray(LamaExpressionBaseNode[]::new));
	}
	
	private List<LamaExpressionBaseNode> processTopLevelVarInits(List<VarInitContext> varInitList) {
		return varInitList.stream().map(varInit -> {
			String varName = varInit.LIDENT().getText();
			globalVars.add(varName);
			
			LamaExpressionBaseNode assignExpr = varInit.assignExpression() != null ? 
					varInit.assignExpression().accept(this)
					: new LamaNullLiteralExpressionNode()
					;
			
			return (LamaExpressionBaseNode) new LamaSetGlobalVariableExpressionNode(varName, assignExpr);
		}).toList();
	}
	
	private LamaExpressionBaseNode processGlobalFunDef(FunDefinitionContext funDefCtx) {
		String funName = funDefCtx.LIDENT().getText();
		globalVars.add(funName);
		
		LamaExpressionBaseNode funExpr = funDefCtx.accept(this);
		
		return new LamaSetGlobalVariableExpressionNode(funName, funExpr);
	}
	
	public LamaExpressionBaseNode visitFunDefinition(FunDefinitionContext ctx) {
		List<PatternContext> patterns = ctx.patternList() != null ? ctx.patternList().pattern() : Collections.emptyList();
		int funArity = patterns.size();
		
		FrameDescriptor.Builder nestedBuilder = FrameDescriptor.newBuilder();
		LamaScope nestedScope = new LamaScope(currentScope, true);
		LamaLanguageSourceParser nestedParser = new LamaLanguageSourceParser(lang, nestedBuilder, nestedScope, globalVars);
		
		List<LamaPatternExpressionNode> patternExprNodes = patterns.stream()
				.map(pat -> (LamaPatternExpressionNode) pat.accept(nestedParser))
				.toList();
		
		LamaExpressionBaseNode bodyExpr = ctx.scopeExpression().accept(nestedParser);
		
		for (int i = funArity - 1; i >= 0; --i) {
			LamaExpressionBaseNode scrutinee = new LamaGetArgumentExpressionNode(i + 1);
			LamaCaseBranchNode branch = new LamaCaseBranchNode(patternExprNodes.get(i), bodyExpr);
			bodyExpr = new LamaCaseExpressionNode(scrutinee, new LamaCaseBranchNode[]{branch});
		}
		
		LamaRootNode funRootNode = new LamaRootNode(lang, nestedBuilder.build(), bodyExpr);
		
		return new LamaFunctionNode(funRootNode.getCallTarget(), funArity);
	}
	
	public LamaExpressionBaseNode visitScopeExpression(ScopeExpressionContext ctx) {
		LamaScope prevScope = currentScope;
		
		currentScope = new LamaScope(prevScope, false);
		
		LamaExpressionBaseNode processedScopeExpr = processScopeExpression(ctx);
		
		currentScope = prevScope;
		
		return processedScopeExpr;
	}
	
	private LamaExpressionBaseNode processScopeExpression(ScopeExpressionContext ctx) {
		ctx.definition().forEach(defCtx -> {
			FunDefinitionContext funDef = defCtx.funDefinition(); 
			
			if (funDef != null) {
				String funName = funDef.LIDENT().getText();
				processVarDecl(funName);
			}
		});
		
		List<LamaExpressionBaseNode> seqNodes = new ArrayList<>();
		ctx.definition().forEach(defCtx -> {
			if (defCtx.varDefinition() != null) {
				seqNodes.addAll(
					processScopedVarInits(defCtx.varDefinition().varInit())
				);
			} else {
				seqNodes.add(processLocalFunDef(defCtx.funDefinition()));
			}
		});
		
		LamaExpressionBaseNode exprNode = ctx.expression().accept(this);
		seqNodes.add(exprNode);
		
		return seqNodes.size() == 1
				? seqNodes.get(0)
				: new LamaSequenceExpressionNode(seqNodes.toArray(LamaExpressionBaseNode[]::new));
	}
	
	@Override
	public LamaExpressionBaseNode visitCaseExpression(CaseExpressionContext ctx) {
		LamaExpressionBaseNode scrutinee = ctx.expression().accept(this);
		List<LamaCaseBranchNode> branches = new ArrayList<>();
		
		ctx.caseBranch().forEach(branchCtx -> {
			LamaScope prevScope = currentScope;
			
			for (int i = 0; i < branchCtx.expression().size(); ++i) {
				LamaPatternExpressionNode patternExpr = (LamaPatternExpressionNode) branchCtx.pattern(i).accept(this);
				LamaExpressionBaseNode expressionExpr = branchCtx.expression(i).accept(this);
				
				branches.add(
					new LamaCaseBranchNode(patternExpr, expressionExpr)
				);
			}
			
			currentScope = prevScope;
		});
		
		return new LamaCaseExpressionNode(scrutinee, branches.toArray(LamaCaseBranchNode[]::new));
	}
	
	@Override
    public LamaExpressionBaseNode visitLetExpression(LetExpressionContext ctx) {
		LamaExpressionBaseNode valueExpr = ctx.expression(0).accept(this);

        LamaScope prevScope = currentScope;
        
        LamaPatternExpressionNode patternNode = (LamaPatternExpressionNode) ctx.pattern().accept(this); // This will add vars to ctx
        LamaExpressionBaseNode bodyNode = ctx.expression(1).accept(this);

        LamaCaseBranchNode branch = new LamaCaseBranchNode(patternNode, bodyNode);
        
        currentScope = prevScope;
        return new LamaCaseExpressionNode(valueExpr, new LamaCaseBranchNode[]{branch});
    }
	
	@Override
	public LamaExpressionBaseNode visitPatternValTag(PatternValTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.VAL);
	}
	
	@Override
	public LamaExpressionBaseNode visitPatternFunTag(PatternFunTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.FUN);
	}
	
	@Override
	public LamaExpressionBaseNode visitPatternStrTag(PatternStrTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.STR);
	}
	
	@Override
	public LamaExpressionBaseNode visitPatternArrayTag(PatternArrayTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.ARRAY);
	}
	
	@Override
	public LamaExpressionBaseNode visitPatternSexpTag(PatternSexpTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.SEXP);
	}
	
	@Override
	public LamaExpressionBaseNode visitPatternBoxTag(PatternBoxTagContext ctx) {
		return new LamaTagPatternExpressionNode(PatternTag.BOX);
	}
	
	@Override
	public LamaExpressionBaseNode visitParenPattern(ParenPatternContext ctx) {
		return ctx.pattern().accept(this);
	}
	
	@Override
	public LamaExpressionBaseNode visitArrayPattern(ArrayPatternContext ctx) {
		LamaPatternExpressionNode[] patterns = Optional.ofNullable(ctx.patternList()).map(t -> t.pattern())
			.orElse(Collections.emptyList())
			.stream()
			.map(pat -> pat.accept(this))
			.toArray(LamaPatternExpressionNode[]::new);
		
		return new LamaArrayPatternExpressionNode(patterns);
	}
	
	@Override
	public LamaExpressionBaseNode visitListPattern(ListPatternContext ctx) {
		LamaPatternExpressionNode result = new LamaSExpPatternExpressionNode("Nil", new LamaPatternExpressionNode[0]);
        
		if (ctx.patternList() != null) {
            for (int i = ctx.patternList().pattern().size() - 1; i >= 0; --i) {
            	LamaPatternExpressionNode head = (LamaPatternExpressionNode) ctx.patternList().pattern(i).accept(this);
            	
                result = new LamaSExpPatternExpressionNode("Cons", new LamaPatternExpressionNode[]{head, result});
            }
        }
        
        return result;
	}
	
	@Override
    public LamaExpressionBaseNode visitConsPattern(ConsPatternContext ctx) {
		LamaPatternExpressionNode head = (LamaPatternExpressionNode) ctx.pattern(0).accept(this);
		LamaPatternExpressionNode tail = (LamaPatternExpressionNode) ctx.pattern(1).accept(this);
		
        return new LamaSExpPatternExpressionNode("Cons", new LamaPatternExpressionNode[] { head, tail });
    }
	
	@Override
    public LamaExpressionBaseNode visitVarPattern(VarPatternContext ctx) {
        String name = ctx.LIDENT().getText();

        int slot = builder.addSlot(FrameSlotKind.Object, name, null);
        currentScope.locals.put(name, slot);
        
        return new LamaVarPatternExpressionNode(slot);
    } 
	
	@Override
    public LamaExpressionBaseNode visitSexpPatternNoArgs(SexpPatternNoArgsContext ctx) {
        return new LamaSExpPatternExpressionNode(
	        			ctx.UIDENT().getText(), 
	        			new LamaPatternExpressionNode[0]
	        		);
    }
	
	@Override
    public LamaExpressionBaseNode visitSexpPatternWithArgs(SexpPatternWithArgsContext ctx) {
        String name = ctx.UIDENT().getText();
        
        LamaPatternExpressionNode[] patternExprs = ctx.patternList().pattern()
        	.stream()
        	.map(pat -> (LamaPatternExpressionNode) pat.accept(this))
        	.toArray(LamaPatternExpressionNode[]::new);
        
        return new LamaSExpPatternExpressionNode(name, patternExprs);
    }
	
	@Override
    public LamaExpressionBaseNode visitWildcardPattern(WildcardPatternContext ctx) {
        return new LamaWildcardPatternExpressionNode();
    }

    @Override
    public LamaExpressionBaseNode visitDecimalPattern(DecimalPatternContext ctx) {
        long value = Long.parseLong(ctx.DECIMAL().getText());
        
        return new LamaNumberPatternExpressionNode(value);
    }
    
    @Override
    public LamaExpressionBaseNode visitStringPattern(StringPatternContext ctx) {
        String text = ctx.STRING().getText();
        String content = text.substring(1, text.length() - 1).replace("\"\"", "\"");
        
        return new LamaStringPatternExpressionNode(content);
    }
    
    @Override
    public LamaExpressionBaseNode visitCharPattern(CharPatternContext ctx) {
        String text = ctx.CHAR().getText();
        char ch = parseLamaCharacter(text, ctx);
        
        return new LamaCharPatternExpressionNode(ch);
    }
    
    @Override
	public LamaExpressionBaseNode visitAliasPattern(AliasPatternContext ctx) {
		String alias = ctx.LIDENT().getText();
    
		int slot = builder.addSlot(FrameSlotKind.Object, alias, null);
        currentScope.locals.put(alias, slot);
        
    	LamaPatternExpressionNode innerPattern = (LamaPatternExpressionNode) ctx.pattern().accept(this);
    	
    	return new LamaAliasPatternExpressionNode(slot, innerPattern);
	}
    
    @Override
	public LamaExpressionBaseNode visitTruePattern(TruePatternContext ctx) {
		return new LamaBoolConstPatternExpressionNode(true);
	}
	
	@Override
	public LamaExpressionBaseNode visitFalsePattern(FalsePatternContext ctx) {
		return new LamaBoolConstPatternExpressionNode(false);
	}
	
	private LamaExpressionBaseNode processLocalVarDef(VarDefinitionContext varDefCtx) {
		List<LamaExpressionBaseNode> localVarInitExprs = processScopedVarInits(varDefCtx.varInit());
		
		return localVarInitExprs.size() == 1
				? localVarInitExprs.get(0)
				: new LamaSequenceExpressionNode(localVarInitExprs)
				;
	}
	
	private LamaExpressionBaseNode processLocalFunDef(FunDefinitionContext funDefCtx) {
		String funName = funDefCtx.LIDENT().getText();
		
		if (!currentScope.locals.containsKey(funName)) {
			processVarDecl(funName);
		}
		
		int index = currentScope.locals.get(funName);
		
		LamaExpressionBaseNode funExpr = funDefCtx.accept(this);
		
		return new LamaSetLocalVariableExpressionNode(index, funExpr);
	}
	
	private List<LamaExpressionBaseNode> processScopedVarInits(List<VarInitContext> varInitList) {
		return varInitList.stream().map(varInit -> {
			String varName = varInit.LIDENT().getText();
			processVarDecl(varName);
			
			LamaExpressionBaseNode assignExpr = varInit.assignExpression() != null ? 
					varInit.assignExpression().accept(this)
					: new LamaNullLiteralExpressionNode()
					;
			
			int slot = currentScope.locals.get(varName);
			return (LamaExpressionBaseNode) new LamaSetLocalVariableExpressionNode(slot, assignExpr);
		}).toList();
	}
	
	@Override
	public LamaExpressionBaseNode visitAssignExpression(AssignExpressionContext ctx) {
		LamaExpressionBaseNode lhsExpr = ctx.consExpression().accept(this);
		
		if (ctx.ASSIGN() == null) {
			return lhsExpr;
		}
		
		LamaExpressionBaseNode rhsExpr = ctx.assignExpression().accept(this);
		
		return processAssignExpression(lhsExpr, rhsExpr, ctx);
	}
	
	@Override
	public LamaExpressionBaseNode visitWhileLoop(WhileLoopContext ctx) {
		LamaExpressionBaseNode condition = ctx.sequenceExpression().accept(this);
		LamaExpressionBaseNode body = ctx.scopeExpression().accept(this);
		
        return new LamaWhileExpressionNode(condition, body);
    }
	
	@Override
	public LamaExpressionBaseNode visitDoWhileLoop(DoWhileLoopContext ctx) {
		LamaScope prevScope = currentScope;
		currentScope = new LamaScope(currentScope, false);
		
		LamaExpressionBaseNode bodyExpr = processScopeExpression(ctx.scopeExpression());
		LamaExpressionBaseNode condExpr = ctx.sequenceExpression().accept(this);
		
		currentScope = prevScope;
		
		return new LamaDoWhileExpressionNode(condExpr, bodyExpr);
	}
	
	@Override
	public LamaExpressionBaseNode visitForLoop(ForLoopContext ctx) {
		LamaScope prevScope = currentScope;
		currentScope = new LamaScope(currentScope, false);
		
		LamaExpressionBaseNode initExpr = Optional.ofNullable(ctx.forInit())
				.map(t -> processScopeExpression(t.scopeExpression()))
				.orElse(new LamaNullLiteralExpressionNode())
				;
		LamaExpressionBaseNode bodyExpr = ctx.scopeExpression().accept(this);
		
		LamaExpressionBaseNode conditionExpr = Optional.ofNullable(ctx.expression())
				.map(t -> t.accept(this))
				.orElse(new LamaIntegerLiteralExpressionNode(1L))
				;
		LamaExpressionBaseNode stepExpr = Optional.ofNullable(ctx.forStep())
				.map(t -> t.accept(this))
				.orElse(new LamaNullLiteralExpressionNode())
				;
		
		currentScope = prevScope;
		
		return new LamaSequenceExpressionNode(
					new LamaExpressionBaseNode[] {
						initExpr,
						new LamaWhileExpressionNode(
							conditionExpr, 
							new LamaSequenceExpressionNode(
								new LamaExpressionBaseNode[] {
										bodyExpr,
										stepExpr
								}
							)
						)	
					}
				);
	}
	
	private LamaExpressionBaseNode processAssignExpression(LamaExpressionBaseNode lhsExpr, LamaExpressionBaseNode rhsExpr, ParserRuleContext ctx) {
		if (lhsExpr instanceof LamaGetLocalVariableExpressionNode getLocalVariableExpressionNode) {
			return new LamaSetLocalVariableExpressionNode(
					getLocalVariableExpressionNode.getSlot(),
					rhsExpr
				);
		} else if (lhsExpr instanceof LamaGetClosureExpressionNode getClosureExpressionNode) {
			return new LamaSetClosureExpressionNode(
					getClosureExpressionNode.getDepth(),
					getClosureExpressionNode.getSlot(),
					rhsExpr
				);
		} else if (lhsExpr instanceof LamaGetGlobalVariableExpressionNode getGlobalVariableExpressionNode) {
			return new LamaSetGlobalVariableExpressionNode(
					getGlobalVariableExpressionNode.getName(),
					rhsExpr
				);
		} else if (lhsExpr instanceof LamaGetArgumentExpressionNode getArgumentExpressionNode) {
			return new LamaSetArgumentExpressionNode(
					getArgumentExpressionNode.getIndex(),
					rhsExpr
				);
		} else if (lhsExpr instanceof LamaGetArrayElementExpressionNode getArrayElementExpressionNode) {
			return new LamaSetArrayElementExpressionNode(
					getArrayElementExpressionNode.getArrayExprNode(),
					getArrayElementExpressionNode.getIndexExprNode(),
					rhsExpr
				);
		} else if (lhsExpr instanceof LamaSequenceExpressionNode sequenceExpressionNode) {
			int exprs = sequenceExpressionNode.getExpressions().length;
			LamaExpressionBaseNode[] expressions = sequenceExpressionNode.getExpressions();
			LamaExpressionBaseNode[] newSeqExprs = expressions.clone();
			newSeqExprs[exprs - 1] = processAssignExpression(expressions[exprs - 1], rhsExpr, ctx);
			
			return new LamaSequenceExpressionNode(newSeqExprs);
		} else if (lhsExpr instanceof LamaIfThenElseExpressionNode ifThenElseExpressionNode) {
			LamaExpressionBaseNode newThen = processAssignExpression(ifThenElseExpressionNode.getThenNode(), rhsExpr, ctx);
			LamaExpressionBaseNode newElse = processAssignExpression(ifThenElseExpressionNode.getElseNode(), rhsExpr, ctx);
			
			return new LamaIfThenElseExpressionNode(
						ifThenElseExpressionNode.getConditionNode(),
						newThen,
						Arrays.asList(ifThenElseExpressionNode.getElifConditions()),
						Arrays.asList(ifThenElseExpressionNode.getElifBodies()),
						newElse
					);
		}
		
//		System.out.println("");
		
		throw new LamaLanguageParseException("LOL", ctx);
	}
	
	@Override
	public LamaExpressionBaseNode visitSequenceExpression(SequenceExpressionContext ctx) {
		return new LamaSequenceExpressionNode(
					ctx.assignExpression().stream().map(expr -> expr.accept(this)).toList()
				);
	}
	
	@Override
    public LamaExpressionBaseNode visitLogicalOrExpression(LogicalOrExpressionContext ctx) {
		LamaExpressionBaseNode result = ctx.logicalAndExpression(0).accept(this);
        
        int exprsCount = ctx.logicalAndExpression().size();
        
        for (int i = 1; i < exprsCount; i++) {
            result = LamaOrNodeGen.create(result, ctx.logicalAndExpression(i).accept(this));
        }
        
        return result;
    }

    @Override
    public LamaExpressionBaseNode visitLogicalAndExpression(LogicalAndExpressionContext ctx) {
        LamaExpressionBaseNode result = ctx.equalityExpression(0).accept(this);
        
        int exprsCount = ctx.equalityExpression().size();
        
        for (int i = 1; i < exprsCount; i++) {
            result = LamaAndNodeGen.create(result, ctx.equalityExpression(i).accept(this));
        }
        
        return result;
    }
	
	@Override
	public LamaExpressionBaseNode visitEqualityExpression(EqualityExpressionContext ctx) {
		LamaExpressionBaseNode result = ctx.relationalExpression(0).accept(this);
		
		int exprsCount = ctx.relationalExpression().size();
		
		for (int i = 1; i < exprsCount; ++i) {
			LamaExpressionBaseNode expr = ctx.relationalExpression(i).accept(this);
			
			if (ctx.EQ(i - 1) != null) {
				result = new LamaEqualExpressionNode(result, expr);
			} else if (ctx.NE(i - 1) != null) {
				result = new LamaNotEqualExpressionNode(result, expr);
			}
		}
		
		return result;
	}
	
	@Override
	public LamaExpressionBaseNode visitRelationalExpression(RelationalExpressionContext ctx) {
		LamaExpressionBaseNode result = ctx.additiveExpression(0).accept(this);
		
		int exprsCount = ctx.additiveExpression().size();
		
		for (int i = 1; i < exprsCount; ++i) {
			LamaExpressionBaseNode expr = ctx.additiveExpression(i).accept(this);
			
			if (ctx.LT(i - 1) != null) {
				result = new LamaLessExpressionNode(result, expr);
			} else if (ctx.LE(i - 1) != null) {
				result = new LamaLessOrEqualExpressionNode(result, expr);
			} else if (ctx.GE(i - 1) != null) {
				result = new LamaGreaterOrEqualExpressionNode(result, expr);
			} else if (ctx.GT(i - 1) != null) {
				result = new LamaGreaterExpressionNode(result, expr);
			}
		}
		
		return result;
	}
	
	@Override
	public LamaExpressionBaseNode visitAdditiveExpression(AdditiveExpressionContext ctx) {
		LamaExpressionBaseNode result = ctx.multiplicativeExpression(0).accept(this);
		
		int exprsCount = ctx.multiplicativeExpression().size();
		
		for (int i = 1; i < exprsCount; ++i) {
			LamaExpressionBaseNode expr = ctx.multiplicativeExpression(i).accept(this);
			
			if (ctx.PLUS(i - 1) != null) {
				result = LamaAddNodeGen.create(result, expr);
			} else if (ctx.MINUS(i - 1) != null) {
				result = LamaSubNodeGen.create(result, expr);
			}
		}
		
		return result;
	}
	
	@Override
	public LamaExpressionBaseNode visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
		LamaExpressionBaseNode result = ctx.prefixExpression(0).accept(this);
		
		int exprsCount = ctx.prefixExpression().size();
		
		for (int i = 1; i < exprsCount; ++i) {
			LamaExpressionBaseNode expr = ctx.prefixExpression(i).accept(this);
			
			if (ctx.MUL(i - 1) != null) {
				result = LamaMulNodeGen.create(result, expr);
			} else if (ctx.DIV(i - 1) != null) {
				result = LamaDivNodeGen.create(result, expr);
			} else if (ctx.MOD(i - 1) != null) {
				result = LamaModNodeGen.create(result, expr);
			}
		}
		
		return result;
	}
	
	@Override
    public LamaExpressionBaseNode visitPrefixExpression(PrefixExpressionContext ctx) {
		LamaExpressionBaseNode exprNode = ctx.postfixExpression().accept(this);
		
		return ctx.MINUS() != null ? new LamaNegExpressionNode(exprNode) : exprNode;
	}
	
	@Override
    public LamaExpressionBaseNode visitConsExpression(ConsExpressionContext ctx) {
        LamaExpressionBaseNode head = visit(ctx.logicalOrExpression());
        
        if (ctx.COLON() == null) {
        	return head;
        }
        
        LamaExpressionBaseNode tail = visit(ctx.consExpression());

        // h : t -> Cons(h, t)
        return new LamaSExpExpressionNode("Cons", new LamaExpressionBaseNode[]{head, tail});
    }
	
	@Override
	public LamaExpressionBaseNode visitLambdaExpression(LambdaExpressionContext ctx) {
		List<PatternContext> patterns = ctx.patternList() != null ? ctx.patternList().pattern() : Collections.emptyList();
		int funArity = patterns.size();
		
		FrameDescriptor.Builder nestedBuilder = FrameDescriptor.newBuilder();
		LamaScope nestedScope = new LamaScope(currentScope, true);
		LamaLanguageSourceParser nestedParser = new LamaLanguageSourceParser(lang, nestedBuilder, nestedScope, globalVars);
		
		List<LamaPatternExpressionNode> patternExprNodes = patterns.stream()
				.map(pat -> (LamaPatternExpressionNode) pat.accept(nestedParser))
				.toList();
		
		LamaExpressionBaseNode bodyExpr = ctx.scopeExpression().accept(nestedParser);
		
		for (int i = funArity - 1; i >= 0; --i) {
			LamaExpressionBaseNode scrutinee = new LamaGetArgumentExpressionNode(i + 1);
			LamaCaseBranchNode branch = new LamaCaseBranchNode(patternExprNodes.get(i), bodyExpr);
			bodyExpr = new LamaCaseExpressionNode(scrutinee, new LamaCaseBranchNode[]{branch});
		}
		
		LamaRootNode funRootNode = new LamaRootNode(lang, nestedBuilder.build(), bodyExpr);
		
		return new LamaFunctionNode(funRootNode.getCallTarget(), funArity);
	}
	
	@Override
	public LamaExpressionBaseNode visitCallExpr(CallExprContext ctx) {
		LamaExpressionBaseNode callee = ctx.postfixExpression().accept(this);
		
		List<LamaExpressionBaseNode> args = List.of();
		
		if (ctx.argumentList() != null) {
			args = ctx.argumentList().expression()
					.stream()
					.map(expr -> expr.accept(this)).toList();
		}
		
		return new LamaCallExpressionNode(callee, args);
	}
	
	@Override
	public LamaExpressionBaseNode visitAccessExpr(AccessExprContext ctx) {
		LamaExpressionBaseNode arrayExpr = ctx.postfixExpression().accept(this);
		LamaExpressionBaseNode indexExpr = ctx.expression().accept(this);
		
		return new LamaGetArrayElementExpressionNode(arrayExpr, indexExpr);
	}
	
	@Override
	public LamaExpressionBaseNode visitDotExpr(DotExprContext ctx) {
		LamaExpressionBaseNode lhsExpr = ctx.postfixExpression().accept(this);
		LamaExpressionBaseNode funExpr = resolveIdenToExpr(ctx.LIDENT().getText());
		
		if (funExpr == null) {
			throw new LamaLanguageParseException("undefined function '" + ctx.LIDENT().getText() + "'", ctx.LIDENT().getSymbol());
		}
		
		return new LamaCallExpressionNode(funExpr, Arrays.asList(lhsExpr));
	}
	
	@Override
	public LamaExpressionBaseNode visitIfExpression(IfExpressionContext ctx) {
		LamaExpressionBaseNode conditionExpr = ctx.expression(0).accept(this);
		LamaExpressionBaseNode thenExpr = ctx.scopeExpression(0).accept(this);
		
		int conditionsCount = ctx.expression().size();
		
		List<LamaExpressionBaseNode> elifConditions = new ArrayList<LamaExpressionBaseNode>();
		List<LamaExpressionBaseNode> elifExprs = new ArrayList<LamaExpressionBaseNode>();
		
		for (int i = 1; i < conditionsCount; ++i) {
			elifConditions.add(ctx.expression(i).accept(this));
			elifExprs.add(ctx.scopeExpression(i).accept(this));
		}
		
		int scopeExpressionsCount = ctx.scopeExpression().size();
		LamaExpressionBaseNode elseExpr = null;
		
		if (scopeExpressionsCount > 1 && conditionsCount < scopeExpressionsCount) {
			elseExpr = ctx.scopeExpression(scopeExpressionsCount - 1).accept(this);
		}
		
		return new LamaIfThenElseExpressionNode(
					conditionExpr, thenExpr, 
					elifConditions, elifExprs, 
					elseExpr
				);
	}
	
	@Override
	public LamaExpressionBaseNode visitLiteral(LiteralContext ctx) {
		String text = ctx.getText();
		
		if (ctx.DECIMAL() != null) {
			return new LamaIntegerLiteralExpressionNode(Long.parseLong(text));
		} else if (ctx.TRUE() != null) {
			return new LamaIntegerLiteralExpressionNode(1L);
		} else if (ctx.FALSE() != null) {
			return new LamaIntegerLiteralExpressionNode(0L);
		} else if (ctx.STRING() != null) {
			String content = text.substring(1, text.length() - 1);
			return new LamaStringLiteralExpressionNode(content);
		} else if (ctx.CHAR() != null) {
			char ch = parseLamaCharacter(text, ctx);
			
			return new LamaCharLiteralExpressionNode(ch);
		}
		
		return null;
	}
	
	@Override
	public LamaExpressionBaseNode visitSkip(SkipContext ctx) {
		return new LamaNullLiteralExpressionNode();
	}
	
	private char parseLamaCharacter(String content, ParserRuleContext ctx) {
		char ch;
		
		content = content.substring(1, content.length() - 1);
		
		if (content.startsWith("\\")) {
			switch (content) {
				case "\\\n":
					ch = '\n';
					break;
				case "\\\t":
					ch = '\t';
					break;
				case "\\\\":
					ch = '\\';
					break;
				default:
					throw new LamaLanguageParseException("Invalid escape character literal: " + content, ctx);
			}
		} else {
			ch = Utils.getOrThrow(
						content.length() == 1, 
						content.charAt(0), 
						new LamaLanguageParseException("Invalid character literal: " + content, ctx)
					);
		}
		
		return ch;
	}
	
	@Override
	public LamaExpressionBaseNode visitParenPrimary(ParenPrimaryContext ctx) {
		return ctx.scopeExpression().accept(this);
	}
	
	@Override
	public LamaExpressionBaseNode visitSexpExpr(SexpExprContext ctx) {
		String constructor = ctx.sexp().UIDENT().getText();
		LamaExpressionBaseNode[] sexpArgExprs = Optional.ofNullable(ctx.sexp().expression()).orElse(Collections.emptyList())
			.stream()
			.map(t -> t.accept(this))
			.toArray(LamaExpressionBaseNode[]::new)
			;
		
		return new LamaSExpExpressionNode(constructor, sexpArgExprs);
	}
	
	@Override
	public LamaExpressionBaseNode visitArrayLiteral(ArrayLiteralContext ctx) {
		return new LamaArrayLiteralExpressionNode(
					ctx.expression()
						.stream()
						.map(expr -> expr.accept(this))
						.toArray(LamaExpressionBaseNode[]::new)
				);
	}
	
	@Override
	public LamaExpressionBaseNode visitListLiteral(ListLiteralContext ctx) {
		LamaExpressionBaseNode result = new LamaSExpExpressionNode("Nil", new LamaExpressionBaseNode[0]);
		
		if (ctx.expression() != null) {
			for (int i = ctx.expression().size() - 1; i >= 0; --i) {
				LamaExpressionBaseNode head = ctx.expression(i).accept(this);
                result = new LamaSExpExpressionNode("Cons", new LamaExpressionBaseNode[]{head, result});
			}
		}
		
		return result;
	}
	
	@Override
	public LamaExpressionBaseNode visitIden(IdenContext ctx) {
		String iden = ctx.identifier().getText();
		
		return Optional.ofNullable(resolveIdenToExpr(iden))
				.orElseThrow(() -> new LamaLanguageParseException("Undefined variable " + iden));
	}
	
	private void processDefinition(DefinitionContext ctx, Consumer<VarDefinitionContext> varDefConsumer, Consumer<FunDefinitionContext> funDefConsumer) {
		if (ctx != null) {
			Optional.ofNullable(ctx.funDefinition()).ifPresentOrElse(funDefConsumer, () -> varDefConsumer.accept(ctx.varDefinition()));
		}
	}
	
	private LamaExpressionBaseNode resolveIdenToExpr(String iden) {
		int[] resolved = currentScope.resolve(iden);
		
		if (resolved != null) {
			int depth = resolved[0];
			int slot = resolved[1];
			
			return depth == 0 
					? new LamaGetLocalVariableExpressionNode(slot)
					: new LamaGetClosureExpressionNode(depth, slot)
					;
		}
		
		if (globalVars.contains(iden)) {
			return new LamaGetGlobalVariableExpressionNode(iden);
		}
		
		return null;
	}
}
