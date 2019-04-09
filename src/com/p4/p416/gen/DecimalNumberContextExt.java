package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import  com.p4.p416.*;

public class DecimalNumberContextExt extends AbstractBaseExt {

	public DecimalNumberContextExt(DecimalNumberContext ctx) {
		super(ctx);
	}

	@Override
	public  DecimalNumberContext getContext(){
		return (DecimalNumberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DecimalNumberContext getContext(String str){
		return (DecimalNumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).decimalNumber());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DecimalNumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DecimalNumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
