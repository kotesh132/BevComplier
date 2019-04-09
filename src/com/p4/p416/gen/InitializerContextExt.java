package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class InitializerContextExt extends AbstractBaseExt {

	public InitializerContextExt(InitializerContext ctx) {
		super(ctx);
	}

	@Override
	public  InitializerContext getContext(){
		return (InitializerContext)contexts.get(contexts.size()-1);
	}

	@Override
	public InitializerContext getContext(String str){
		return (InitializerContext)new PopulateExtendedContextVisitor().visit(getParser(str).initializer());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof InitializerContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ InitializerContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
