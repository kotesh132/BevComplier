package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.TupleKeySetExpressionContext;

public class TupleKeySetExpressionContextExt extends AbstractBaseExt {

	public TupleKeySetExpressionContextExt(TupleKeySetExpressionContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TupleKeySetExpressionContext getContext(){
		return (TupleKeySetExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TupleKeySetExpressionContext getContext(String str){
		return (TupleKeySetExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).tupleKeySetExpression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TupleKeySetExpressionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TupleKeySetExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
