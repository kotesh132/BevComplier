package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.RealNumberContext;

public class RealNumberContextExt extends AbstractBaseExt {

	public RealNumberContextExt(RealNumberContext ctx) {
		super(ctx);
	}

	@Override
	public  RealNumberContext getContext(){
		return (RealNumberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public RealNumberContext getContext(String str){
		return (RealNumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).realNumber());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof RealNumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ RealNumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
