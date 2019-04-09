package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class KeySetExpressionContextExt extends AbstractBaseExt {

	public KeySetExpressionContextExt(KeySetExpressionContext ctx) {
		super(ctx);
	}

	@Override
	public  KeySetExpressionContext getContext(){
		return (KeySetExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public KeySetExpressionContext getContext(String str){
		return (KeySetExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).keySetExpression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof KeySetExpressionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ KeySetExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
