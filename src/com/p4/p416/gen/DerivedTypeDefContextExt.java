package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class DerivedTypeDefContextExt extends AbstractBaseExt {

	public DerivedTypeDefContextExt(DerivedTypeDefContext ctx) {
		super(ctx);
	}

	@Override
	public  DerivedTypeDefContext getContext(){
		return (DerivedTypeDefContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DerivedTypeDefContext getContext(String str){
		return (DerivedTypeDefContext)new PopulateExtendedContextVisitor().visit(getParser(str).typedefDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DerivedTypeDefContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DerivedTypeDefContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
