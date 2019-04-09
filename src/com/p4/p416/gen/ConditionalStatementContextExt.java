package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416Parser.ConditionalStatementContext;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.IfElseStatementContext;
import com.p4.p416.gen.P416Parser.IfStatementContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.tools.graph.structs.TableMap;

public abstract class ConditionalStatementContextExt extends AbstractBaseExt {

	public ConditionalStatementContextExt(ParserRuleContext context) {
		super(context);
	}

	private static final Logger L = LoggerFactory.getLogger(ConditionalStatementContextExt.class);
	@Override
	public void buildST(TableMap tm){
		/*
		ConditionalStatementContext ctx = (ConditionalStatementContext) getContext();		
		getExtendedContext(ctx.expression()).buildST(tm);
		DummyNode n = (DummyNode) tm.currentControlScope().cg.currentNode;
		DummyNode ifNode = new DummyNode(n.name+"_if");
		ifNode.parent = n;
		DummyNode elseNode = new DummyNode(n.name+"_else");
		elseNode.parent = n;
		n.addCondTableNode(ifNode, elseNode);
		DummyNode leg1 = null, leg2 = null;
		tm.currentControlScope().cg.currentNode = ifNode;
		getExtendedContext(ctx.statement(0)).buildST(tm);
		leg1 = (DummyNode) tm.currentControlScope().cg.currentNode;

		if(ctx.statement().size() > 1){
			tm.currentControlScope().cg.currentNode = elseNode;
			getExtendedContext(ctx.statement(1)).buildST(tm);
			leg2 = (DummyNode) tm.currentControlScope().cg.currentNode;
		}
		DummyNode end = new DummyNode(n.name+"_cond_term");
		end.parent = leg1;
		leg1.children.add(end);
		if(leg2!=null){
			leg2.children.add(end);
		}else{
			elseNode.children.add(end);
		}
		tm.currentControlScope().cg.currentNode = end;
		*/
	}

	@Override
	public StatementType getStatementType()
	{
		return StatementType.CONDITIONAL_STATEMENT;
	}
	
	public void gatherIfStatements(ExpressionContext expCtx, StatementContext stmt1, StatementContext stmt2, List<StatementContext> statementContexts, String prevCond) {
		String ifCond = prevCond.isEmpty() ? getExtendedContext(expCtx).getFormattedText() : prevCond + "&&" + getExtendedContext(expCtx).getFormattedText();
		String notIfCond = prevCond.isEmpty() ? "!(" + getExtendedContext(expCtx).getFormattedText() + ")" : prevCond + "&& !(" + getExtendedContext(expCtx).getFormattedText() + ")";
		if(stmt2 != null) {
			//if elseif else
			if(stmt2.conditionalStatement() != null) {
				StatementContext ifStmt = AbstractBaseExt.getStatement("if("+ifCond+"){"+getExtendedContext(stmt1).getFormattedText()+"}").getContext();
				statementContexts.add(ifStmt);
				
				ConditionalStatementContext cnctx = getExtendedContext(stmt2.conditionalStatement()).getContext();
				//String negatedCond = "!(" + ifCond + ")";
				//String nextCond = prevCond.isEmpty() ? notIfCond : prevCond + "&&" + notIfCond;
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
	
	public void flattenIfStatements() {
		ConditionalStatementContext cnctx = getContext();
		StringBuilder sb = new StringBuilder();
		if(cnctx instanceof IfElseStatementContext){
			List<StatementContext> flattenedIfs = new ArrayList<>();
			gatherIfStatements(((IfElseStatementContext) cnctx).expression(), ((IfElseStatementContext) cnctx).statement(0), ((IfElseStatementContext) cnctx).statement(1), flattenedIfs, "");
			for(StatementContext stmCtx : flattenedIfs) {
				L.info(getExtendedContext(stmCtx).getFormattedText());
				sb.append(getExtendedContext(stmCtx).getFormattedText());
			}
		}
		ConditionalStatementContext condStatementContext = getExtendedContext(cnctx).getContext(sb.toString());
		getExtendedContext(cnctx).setContext(condStatementContext);
	}
}
