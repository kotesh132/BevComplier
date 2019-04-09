package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class DirectionContextExt extends AbstractBaseExt {

	public DirectionContextExt(DirectionContext ctx) {
		super(ctx);
	}

	@Override
	public  DirectionContext getContext(){
		return (DirectionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DirectionContext getContext(String str){
		return (DirectionContext)new PopulateExtendedContextVisitor().visit(getParser(str).direction());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DirectionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DirectionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
