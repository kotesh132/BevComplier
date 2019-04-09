package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;

public class SwitchStatementVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(SwitchStatementVisitor.class);
	boolean switchStatement = false;
	List<String> switchLables = new ArrayList<>();

	@Override
	public ParserRuleContext visitSwitchStatement(P416Parser.SwitchStatementContext ctx) {
		switchStatement = true;
		super.visitSwitchStatement(ctx);
		switchStatement = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitSwitchLabel(P416Parser.SwitchLabelContext ctx) {
		if(switchStatement) {
			String label = ctx.getText();
			if(!switchLables.contains(label)) {
				switchLables.add(label);
			} else {
				L.error("Line:"+ctx.start.getLine()+": duplicate switch label.");
			}
		}
		return ctx;
	}
}
