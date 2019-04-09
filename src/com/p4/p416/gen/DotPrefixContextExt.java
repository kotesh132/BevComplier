package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class DotPrefixContextExt extends AbstractBaseExt {

	public DotPrefixContextExt(DotPrefixContext ctx) {
		super(ctx);
	}

	@Override
	public  DotPrefixContext getContext(){
		return (DotPrefixContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DotPrefixContext getContext(String str){
		return (DotPrefixContext)new PopulateExtendedContextVisitor().visit(getParser(str).dotPrefix());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DotPrefixContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DotPrefixContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
