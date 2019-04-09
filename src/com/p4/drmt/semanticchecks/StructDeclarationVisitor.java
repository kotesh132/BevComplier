package com.p4.drmt.semanticchecks;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;

public class StructDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{

	private static final Logger L = LoggerFactory.getLogger(StructDeclarationVisitor.class);
	String structName = null;

	
	@Override
	public ParserRuleContext visitStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx) {
		if(ctx.name() !=null)
			structName = ctx.name().getText();
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitInstantiation(P416Parser.InstantiationContext ctx) {
		if(structName != null && ctx.typeRef() != null) {
			if(structName.equals(ctx.typeRef().getText()))
			L.error("Line:"+ctx.start.getLine()+": "+ "cannot allocate objects of type struct."+structName);
		}
		return ctx;
	}
}
