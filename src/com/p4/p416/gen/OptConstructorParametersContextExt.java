package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OptConstructorParametersContext;

public class OptConstructorParametersContextExt extends AbstractBaseExt {

	public OptConstructorParametersContextExt(OptConstructorParametersContext ctx) {
		super(ctx);
	}

	@Override
	public  OptConstructorParametersContext getContext(){
		return (OptConstructorParametersContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OptConstructorParametersContext getContext(String str){
		return (OptConstructorParametersContext)new PopulateExtendedContextVisitor().visit(getParser(str).optConstructorParameters());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OptConstructorParametersContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OptConstructorParametersContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
