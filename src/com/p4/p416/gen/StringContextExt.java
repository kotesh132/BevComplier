package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.StringContext;

public class StringContextExt extends ExpressionContextExt {

	public StringContextExt(StringContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  StringContext getContext(){
		return (StringContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StringContext getContext(String str){
		return (StringContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StringContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StringContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
