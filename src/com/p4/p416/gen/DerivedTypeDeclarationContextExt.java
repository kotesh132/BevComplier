package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class DerivedTypeDeclarationContextExt extends AbstractBaseExt {

	public DerivedTypeDeclarationContextExt(DerivedTypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  DerivedTypeDeclarationContext getContext(){
		return (DerivedTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DerivedTypeDeclarationContext getContext(String str){
		return (DerivedTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).derivedTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DerivedTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DerivedTypeDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
