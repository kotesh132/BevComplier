package com.p4.drmt.semanticchecks;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.KeyElementContext;
import com.p4.p416.gen.P416Parser.KeyElementListContext;

public class TableDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(TableDeclarationVisitor.class);
	boolean hasActions = false;
	boolean hasKeys = false;
	boolean table = false;

	@Override 
	public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx){
		table = true;
		super.visitTableDeclaration(ctx);
		table = false;
		if(!hasActions) {
			L.error("Line:"+ctx.start.getLine()+": Table does not have an 'actions' property.");
		} else if(!hasKeys) {
			L.error("Line:"+ctx.start.getLine()+": Table does not have an 'key' property.");
		}
		return ctx;
	}


	@Override 
	public ParserRuleContext visitActions(P416Parser.ActionsContext ctx){
		if(table)
			hasActions =true;
		return ctx;
	}

	@Override 
	public ParserRuleContext visitKey(P416Parser.KeyContext ctx){
		if(table) {
			hasKeys =true;
			if(ctx.keyElementList() != null) {
				List<KeyElementContext> keyElementContextList = ctx.keyElementList().keyElement();
				for(int i=0 ; i < keyElementContextList.size(); i++) {
					String name = keyElementContextList.get(i).name().getText();
					
					if(!name.equalsIgnoreCase("lpm") && !name.equalsIgnoreCase("exact") &&
							!name.equalsIgnoreCase("ternary")) {
						L.error("Line:"+ctx.start.getLine()+": Could not find declaration, should be exact/lpm/ternary.");
					}
				}
			}
		}
		return ctx;
	}
}
