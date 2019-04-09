package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SimpleKeySetExpressionContext;

public class SimpleKeySetExpressionContextExt extends AbstractBaseExt {

	public SimpleKeySetExpressionContextExt(SimpleKeySetExpressionContext ctx) {
		super(ctx);
	}

	@Override
	public  SimpleKeySetExpressionContext getContext(){
		return (SimpleKeySetExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SimpleKeySetExpressionContext getContext(String str){
		return (SimpleKeySetExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).simpleKeySetExpression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SimpleKeySetExpressionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SimpleKeySetExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
