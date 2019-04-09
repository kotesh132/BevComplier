package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OptTypeParametersContext;

public class OptTypeParametersContextExt extends AbstractBaseExt {

	public OptTypeParametersContextExt(OptTypeParametersContext ctx) {
		super(ctx);
	}

	@Override
	public  OptTypeParametersContext getContext(){
		return (OptTypeParametersContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OptTypeParametersContext getContext(String str){
		return (OptTypeParametersContext)new PopulateExtendedContextVisitor().visit(getParser(str).optTypeParameters());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OptTypeParametersContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OptTypeParametersContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
