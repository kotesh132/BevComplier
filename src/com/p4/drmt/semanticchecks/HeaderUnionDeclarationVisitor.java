package com.p4.drmt.semanticchecks;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.StructFieldContextExt;
import com.p4.p416.gen.StructTypeDeclarationContextExt;

public class HeaderUnionDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(HeaderUnionDeclarationVisitor.class);
	boolean headerUnion = false;

	Map<String, StructTypeDeclarationContextExt> structTypeDeclarations = new HashMap<>();

	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		(P4programContextExt.getExtendedContext(ctx)).getStructs(structTypeDeclarations);
		super.visitP4program(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx) {
		headerUnion = true;
		super.visitHeaderUnionDeclaration(ctx);
		headerUnion = false;

		return ctx;
	}

	@Override
	public ParserRuleContext visitStructField(P416Parser.StructFieldContext ctx) {
		if(headerUnion) {
			System.out.println((StructFieldContextExt.getExtendedContext(ctx)).getFormattedText());
			if(ctx.typeRef().baseType()!= null) {
				L.error("Line:"+ctx.start.getLine()+": "+"No non-header field allowed in header_union.");
			} 
			if(ctx.typeRef().typeName() != null) {
				if(structTypeDeclarations.get(ctx.typeRef().typeName().getText()) != null) {
					L.error("Line:"+ctx.start.getLine()+": "+"No Struct field allowed in header_union.");
				}
			}
		}
		return ctx;
	}
}
