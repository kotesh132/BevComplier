package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.p4.p416.iface.IStatement;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.gen.P416Parser.ApplyMethodCallContext;
import com.p4.p416.gen.P416Parser.AssignmentStatementContext;
import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.p416.gen.P416Parser.ConditionalStatementContext;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.IfElseStatementContext;
import com.p4.p416.gen.P416Parser.IfStatementContext;
import com.p4.p416.gen.P416Parser.StatOrDeclListContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.p416.gen.P416Parser.StatementOrDeclarationContext;
import com.p4.p416.gen.P416Parser.CallWithoutTypeArgsContext;

import lombok.Getter;

public class StatementContextExt extends AbstractBaseExt implements IStatement {

	private static final Logger L = LoggerFactory.getLogger(StatementContextExt.class);

	public StatementContextExt(StatementContext ctx) {
		super(ctx);
	}

	@Override
	public  StatementContext getContext(){
		return (StatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StatementContext getContext(String str){
		return (StatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).statement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	private void gatherIfStatements(ExpressionContext expCtx, StatementContext stmt1, StatementContext stmt2, List<StatementContext> statementContexts, String prevCond) {
		String ifCond = prevCond.isEmpty() ? getExtendedContext(expCtx).getFormattedText() : prevCond + "&&" + getExtendedContext(expCtx).getFormattedText();
		String notIfCond = prevCond.isEmpty() ? "(!(" + getExtendedContext(expCtx).getFormattedText() + "))" : prevCond + "&& (!(" + getExtendedContext(expCtx).getFormattedText() + "))";
		if(stmt2 != null) {
			//if elseif else
			if(stmt2.conditionalStatement() != null) {
				StatementContext ifStmt = AbstractBaseExt.getStatement("if("+ifCond+"){"+getExtendedContext(stmt1).getFormattedText()+"}").getContext();
				statementContexts.add(ifStmt);
				
				ConditionalStatementContext cnctx = stmt2.conditionalStatement();
				if(cnctx instanceof IfStatementContext){
					gatherIfStatements(((IfStatementContext)cnctx).expression(), ((IfStatementContext)cnctx).statement(), null, statementContexts, notIfCond);
				}else if(cnctx instanceof IfElseStatementContext){
					gatherIfStatements(((IfElseStatementContext)cnctx).expression(), ((IfElseStatementContext)cnctx).statement(0), ((IfElseStatementContext)cnctx).statement(1), statementContexts, notIfCond);
				}
			}
			//if else
			else{
				StatementContext ifStmt = AbstractBaseExt.getStatement("if("+ifCond+"){"+getExtendedContext(stmt1).getFormattedText()+"}").getContext();
				statementContexts.add(ifStmt);
				
				StatementContext notIfStmt = AbstractBaseExt.getStatement("if("+notIfCond+"){"+getExtendedContext(stmt2).getFormattedText()+"}").getContext();
				statementContexts.add(notIfStmt);
			}
		}
		else{
			StatementContext ifStmt = AbstractBaseExt.getStatement("if("+ifCond+"){"+getExtendedContext(stmt1).getFormattedText()+"}").getContext();
			statementContexts.add(ifStmt);
		}
	}
	
	@Override
	public void flattenIfStatements() {
		StatementContext stctx = getContext();
		if(stctx.conditionalStatement() != null) {
			ConditionalStatementContext cnctx = stctx.conditionalStatement();
			if(cnctx instanceof IfElseStatementContext){
				StringBuilder sb = new StringBuilder();
				List<StatementContext> flattenedIfs = new ArrayList<>();
				gatherIfStatements(((IfElseStatementContext) cnctx).expression(), ((IfElseStatementContext) cnctx).statement(0), ((IfElseStatementContext) cnctx).statement(1), flattenedIfs, "");
				for(StatementContext stmCtx : flattenedIfs) {
					sb.append(getExtendedContext(stmCtx).getFormattedText());
				}
				StatementContextExt stmtCtxE = (StatementContextExt) getExtendedContext(stctx);
				StatementContext statementContext = stmtCtxE.getContext("{" + sb.toString() + "}");
				stmtCtxE.setContext(statementContext);
				((StatementContextExt)getExtendedContext(stmtCtxE.getContext())).flattenIfStatements = true;
			}
			else {
				IfStatementContext ifCtx = (IfStatementContext) cnctx;
				StatementContextExt stmtCtx = null;
				if(ifCtx.statement() != null) {
					stmtCtx = (StatementContextExt) getExtendedContext(((IfStatementContext) cnctx).statement());
					stmtCtx.flattenIfStatements();
				}
			}
		}
		else {
			super.flattenIfStatements();
		}
	}
	
	@Getter
	protected boolean flattenIfStatements = false;
	
	@Override
	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			StatementContext stctx = (StatementContext) getContext();
			boolean isFlattenedIfs = ((StatementContextExt)getExtendedContext(stctx)).isFlattenIfStatements();
			if(stctx.assignmentStatement()!=null ){
				AssignmentStatementContext assignmentStatementContext = stctx.assignmentStatement();
				if(((ExpressionContextExt)getExtendedContext(assignmentStatementContext.expression())).isApplyNode()){
					String tableName = ExpressionContextExt.getApplyPrefix(getExtendedContext(assignmentStatementContext.expression()).getFormattedText());
					Symbol tablectx = resolve(tableName);
					if(tablectx == null){
						throw new RuntimeException("Couldn't resolve :"+tableName);
					}
					if(cfgmap.isInlineTableApply()){
						throw new UnsupportedOperationException("Not implemented");
					}else{
						getExtendedContext(assignmentStatementContext).tableApplyNode = (CFGNode)tablectx;
					}
				}
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(assignmentStatementContext));
			}else if(stctx.methodCallStatement()!=null){
				if(stctx.methodCallStatement() instanceof ApplyMethodCallContext){
					String tableName = getExtendedContext(((ApplyMethodCallContext)stctx.methodCallStatement()).lvalue()).getFormattedText();
					L.debug("Found Table apply "+tableName);
					Symbol tablectx = resolve(tableName);
					if(cfgmap.isInlineTableApply()){
						CFGBuildingBlock ret = CFGBuildingBlock.unitOf(getExtendedContext(stctx.methodCallStatement()));
						this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(ret, ((CFGNode)tablectx).getCFGBuildingBlock());
					}else{
						getExtendedContext(stctx.methodCallStatement()).tableApplyNode = (CFGNode)tablectx;
						this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(stctx.methodCallStatement()));
					}
				}else{
					this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(stctx.methodCallStatement()));
				}
			}else if(stctx.conditionalStatement()!=null){
				ConditionalStatementContext cnctx = stctx.conditionalStatement();
				ExpressionContext e = null;
				List<StatementContext> statementContexts = new ArrayList<>();
				if(cnctx instanceof IfStatementContext){
					e = ((IfStatementContext) cnctx).expression();
					statementContexts.add(((IfStatementContext) cnctx).statement());
				}else if(cnctx instanceof IfElseStatementContext){
					e = ((IfElseStatementContext) cnctx).expression();
					statementContexts.addAll(((IfElseStatementContext) cnctx).statement());
				}
				if(((ExpressionContextExt) getExtendedContext(e)).isApplyNode()){
					//Table apply here
					String tableName = ExpressionContextExt.getApplyPrefix(getExtendedContext(e).getFormattedText());
					Symbol tablectx = resolve(tableName);
					if(tablectx == null){
						throw new RuntimeException("Couldn't resolve :"+tableName);
					}
					if(cfgmap.isInlineTableApply()){
						throw new UnsupportedOperationException("Not implemented");
					}else{
						getExtendedContext(e).tableApplyNode = (CFGNode)tablectx;
					}
				}
				CFGBuildingBlock ret = CFGBuildingBlock.unitOf(getExtendedContext(e));
				Set<CFGBuildingBlock> branches = new LinkedHashSet<>();
				for(StatementContext statementContext:statementContexts){
					branches.add(getExtendedContext(statementContext).buildNGetCFG(cfgmap));
				}
				this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(ret, branches);
			}else if(stctx.emptyStatement()!=null){
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(stctx.emptyStatement()));
			}else if(stctx.blockStatement()!=null){
				if(isFlattenedIfs) {
					Set<CFGBuildingBlock> bldBlocks = new HashSet<>();
					BlockStatementContext blockStatementContext = getExtendedContext(stctx.blockStatement()).getContext();
					if(blockStatementContext.statOrDeclList() != null) {
						StatOrDeclListContext statOrDeclListContext = getExtendedContext(blockStatementContext.statOrDeclList()).getContext();
						if(statOrDeclListContext.statementOrDeclaration() != null) {
							List<StatementOrDeclarationContext> statementOrDeclarationContextList = statOrDeclListContext.statementOrDeclaration();
							for(StatementOrDeclarationContext statementOrDeclarationContext: statementOrDeclarationContextList) {
								
								if(statementOrDeclarationContext.statement() != null) {
									StatementContext statementContext = getExtendedContext(statementOrDeclarationContext.statement()).getContext();
									bldBlocks.add(getExtendedContext(statementContext).buildNGetCFG(cfgmap));
								}
							}
						}
					}
					BlockStatementContextExt blkStmt = AbstractBaseExt.getBlockStatement("{}");
					this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(blkStmt.buildNGetCFG(cfgmap), bldBlocks);
				}
				else {
					this.cFGBuildingBlock = getExtendedContext(stctx.blockStatement()).buildNGetCFG(cfgmap);
				}
			}else if(stctx.returnStatement()!=null){
				throw new UnsupportedOperationException("Not yet implemented");
			}else if(stctx.exitStatement()!=null){
				throw new UnsupportedOperationException("Not yet implemented"); 
			}else if(stctx.switchStatement()!=null){
				throw new UnsupportedOperationException("Not yet implemented");
			}else{
				throw new UnsupportedOperationException("Not yet implemented");
			}
		}
		if(this.cFGBuildingBlock==null){
			//TODO forDEBUG
			throw new RuntimeException();
		}
		return this.cFGBuildingBlock;
	}
	/*@Override
	public void replaceVd(){
		StatementContext stctx =  getContext();
		if(stctx.methodCallStatement()!=null){
			if(stctx.methodCallStatement() instanceof CallWithoutTypeArgsContext){
				CallWithoutTypeArgsContextExt callWithoutTypeArgsContextExt = (CallWithoutTypeArgsContextExt)  stctx.methodCallStatement().extendedContext;
				String assignment = callWithoutTypeArgsContextExt.replaceHeader();
				StatementContext statementContext = getContext(assignment);
				statementContext.extendedContext.setPredicateSymbol(callWithoutTypeArgsContextExt.predicateSymbol);
				setContext(statementContext);
			}
		}else{
			super.replaceVd();
		}

	}*/
}
