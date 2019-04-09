package com.p4.drmt.semanticchecks;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.HeaderTypeDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.StructTypeDeclarationContextExt;
import com.p4.p416.gen.P416Parser.BoolTypeContext;
import com.p4.p416.gen.P416Parser.VarBitSizeTypeContext;

public class HeaderDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(HeaderDeclarationVisitor.class);
	boolean boolfieldheader = false;
	int varBitCounter = 0;
	boolean headertype = false;

	Map<String, StructTypeDeclarationContextExt> structTypeDeclarations = new HashMap<>();


	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		(P4programContextExt.getExtendedContext(ctx)).getStructs(structTypeDeclarations);
		super.visitP4program(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx) {
		headertype = true;
		if(ctx.name().getText().equals("boolfield")){
			boolfieldheader = true;
		}
		super.visitHeaderTypeDeclaration(ctx);
		headertype = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitStructField(P416Parser.StructFieldContext ctx) {
		if(headertype) {
			if(ctx.typeRef()!= null && ctx.typeRef().tupleType() != null) {
				L.error("Line:"+ctx.start.getLine()+": "+"Tuples are not allowed in header.");
			}
			if(boolfieldheader && ctx.typeRef() != null) {
				if (ctx.typeRef().baseType()  instanceof BoolTypeContext) {
					L.error("Line:"+ctx.start.getLine()+": "+"Header boolfield cannot have type bool.");
				}
			}
			if(ctx.typeRef().baseType()  instanceof VarBitSizeTypeContext){
				varBitCounter++;
				if(varBitCounter >1) {
					L.error("Line:"+ctx.start.getLine()+": Multiple varbit fields in a header.");
				}
			} 
			if(structTypeDeclarations.get(ctx.typeRef().getText()) != null) {
				L.error("Line:"+ctx.start.getLine()+": "+"No struct fields are allowed in headers.");
			}
		}
		return ctx;
	}
}
