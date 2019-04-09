package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class DontcareContextExt extends AbstractBaseExt {

	public DontcareContextExt(DontcareContext ctx) {
		super(ctx);
	}

	@Override
	public  DontcareContext getContext(){
		return (DontcareContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DontcareContext getContext(String str){
		return (DontcareContext)new PopulateExtendedContextVisitor().visit(getParser(str).dontcare());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DontcareContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DontcareContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
