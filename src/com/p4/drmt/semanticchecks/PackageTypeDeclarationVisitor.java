package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.PackageTypeDeclarationContextExt;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;
import com.p4.p416.gen.P416Parser.TypeParameterListContext;

public class PackageTypeDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(PackageTypeDeclarationVisitor.class);
	
	List<String> nameList = new ArrayList<>();
//	public void getPackages(Map<String, PackageTypeDeclarationContextExt> packages) {
	
	Map<String, PackageTypeDeclarationContextExt> packages = new HashMap<>();
	
	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		P4programContextExt.getExtendedContext(ctx).getPackages(packages);
		super.visitP4program(ctx);
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx) {
		if(ctx.optTypeParameters() != null) {
			TypeParameterListContext names = ctx.optTypeParameters().typeParameterList();
			for(NonTypeNameContext nameContext :names.nonTypeName()) {
				String name = PackageTypeDeclarationContextExt.getExtendedContext(nameContext).getFormattedText();
				if(nameList.contains(name)) {
					L.error("Line:"+ctx.start.getLine()+": Duplicates declaration.");
				} else {
					nameList.add(name);
				}
				
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitParameter(P416Parser.ParameterContext ctx) {
		if(ctx.typeRef() != null) {
			if(packages.get(ctx.typeRef().getText()) != null) {
				L.error("Line:"+ctx.start.getLine()+": parameter cannot be a package.");
			}
		}
//		super.visitParameter(ctx);
		return ctx;
	}

}
