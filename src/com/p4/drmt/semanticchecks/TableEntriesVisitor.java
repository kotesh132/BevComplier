package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.iface.IKeyElement;

public class TableEntriesVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(TableEntriesVisitor.class);

	Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
	List<IKeyElement> tableKeyElements = new ArrayList<IKeyElement>();
	Integer expectedKeyElementsInTable = 0;
	
	@Override 
	public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx) {
		ctx.extendedContext.getActions(actions);
		super.visitControlDeclaration(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx){
		tableKeyElements = ctx.extendedContext.getKeyElements();
		super.visitTableDeclaration(ctx);
		tableKeyElements=null;
		return ctx;
	}

	@Override
	public ParserRuleContext visitEntry(P416Parser.EntryContext ctx){
		super.visitEntry(ctx);
		if(tableKeyElements.size() > expectedKeyElementsInTable) {
			L.error("Line:"+ctx.start.getLine()+": syntax error, unexpected ), expecting \",\"");
		}else if(tableKeyElements.size() < expectedKeyElementsInTable) {
			L.error("Line:"+ctx.start.getLine()+": Entry: Cannot match tuples with different sizes 1 vs 2");
		}
		if(!actions.containsKey(ctx.actionRef().extendedContext.getActionName())) {
			L.error("Line:"+ctx.start.getLine()+": Could not find declaration for "+ctx.actionRef().extendedContext.getActionName());
		}
		expectedKeyElementsInTable = 0;
		return ctx;
	}

	@Override
	public ParserRuleContext visitSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx){
		expectedKeyElementsInTable = expectedKeyElementsInTable+1;
		super.visitSimpleKeySetExpression(ctx);
		return ctx;
	}


}
