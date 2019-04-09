package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.gen.P416Parser.PlusPlusContext;

public class PlusPlusContextExt extends ExpressionContextExt {

	public PlusPlusContextExt(PlusPlusContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  PlusPlusContext getContext(){
		return (PlusPlusContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlusPlusContext getContext(String str){
		return (PlusPlusContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PlusPlusContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PlusPlusContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext(getContext().expression(1));}
	@Override
	public String getOperator() {
		return getContext().PP().getText().trim();
	}
}
