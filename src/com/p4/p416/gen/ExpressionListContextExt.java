package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.ExpressionListContext;

public class ExpressionListContextExt extends AbstractBaseExt {

	public ExpressionListContextExt(ExpressionListContext ctx) {
		super(ctx);
	}

	@Override
	public  ExpressionListContext getContext(){
		return (ExpressionListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ExpressionListContext getContext(String str){
		return (ExpressionListContext)new PopulateExtendedContextVisitor().visit(getParser(str).expressionList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExpressionListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExpressionListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	public void getExps(List<String> exps) {
		ExpressionListContext ctx = (ExpressionListContext) getContext();
		for(ExpressionContext e: ctx.expression()){
			exps.add(getExtendedContext(e).getFormattedText());
		}
	}
}
