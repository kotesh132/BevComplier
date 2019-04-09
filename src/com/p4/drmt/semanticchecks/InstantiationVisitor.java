package com.p4.drmt.semanticchecks;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.InstantiationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.PackageTypeDeclarationContextExt;

public class InstantiationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(InstantiationVisitor.class);
	boolean isPresent = false;

	/// Instance names cannot be don't care
	/// Do not declare instances in apply {} blocks, parser states or actions

	Map<String, PackageTypeDeclarationContextExt> packages = new HashMap<>();

	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		(P4programContextExt.getExtendedContext(ctx)).getPackages(packages);
		super.visitP4program(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitActionDeclaration(P416Parser.ActionDeclarationContext ctx) {
		isPresent = true;
		super.visitActionDeclaration(ctx);
		isPresent = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitControlBody(P416Parser.ControlBodyContext ctx) {
		isPresent = true;
		super.visitControlBody(ctx);
		isPresent = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitParserStates(P416Parser.ParserStatesContext ctx) {
		isPresent = true;
		super.visitParserStates(ctx);
		isPresent = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitInstantiation(P416Parser.InstantiationContext ctx) {
		if(isPresent) {
			L.error("Line:"+ctx.start.getLine()+": Instantiations not allowed in apply::"+ InstantiationContextExt.getExtendedContext(ctx).getFormattedText());
		}
		int pkgParamCnt,instArgCnt = 0 ;
		PackageTypeDeclarationContextExt iPackage = packages.get(((InstantiationContextExt) InstantiationContextExt.getExtendedContext(ctx)).getTypeRef().getPrefixedType());
		if( iPackage != null) {
			pkgParamCnt = iPackage.getParameters().size();
			instArgCnt = ((InstantiationContextExt) InstantiationContextExt.getExtendedContext(ctx)).getArguments().size();
			if(pkgParamCnt != instArgCnt) {
				//The logic is incorrect. Needs rewrite.
//				L.error("Line:"+ctx.start.getLine()+": "+ "Passing "+instArgCnt+" arguments when "+pkgParamCnt+" expected.");
			}
		}
		
		return ctx;
		
	}

}
