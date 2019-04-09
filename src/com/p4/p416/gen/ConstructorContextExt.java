package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ConstructorContext;

public class ConstructorContextExt extends ExpressionContextExt {

	public ConstructorContextExt(ConstructorContext ctx) {
		super(ctx);
	}

	@Override
	public  ConstructorContext getContext(){
		return (ConstructorContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ConstructorContext getContext(String str){
		return (ConstructorContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ConstructorContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ConstructorContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
