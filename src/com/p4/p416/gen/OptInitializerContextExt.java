package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OptInitializerContext;

public class OptInitializerContextExt extends AbstractBaseExt {

	public OptInitializerContextExt(OptInitializerContext ctx) {
		super(ctx);
	}

	@Override
	public  OptInitializerContext getContext(){
		return (OptInitializerContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OptInitializerContext getContext(String str){
		return (OptInitializerContext)new PopulateExtendedContextVisitor().visit(getParser(str).optInitializer());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OptInitializerContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OptInitializerContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
