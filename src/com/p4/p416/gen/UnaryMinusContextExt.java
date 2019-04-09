package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.UnaryExpression_minusContext;
import com.p4.p416.gen.P416Parser.UnaryMinusContext;

public class UnaryMinusContextExt extends ExpressionContextExt {

	public UnaryMinusContextExt(UnaryMinusContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  UnaryMinusContext getContext(){
		return (UnaryMinusContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public UnaryMinusContext getContext(String str){
		return (UnaryMinusContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof UnaryMinusContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryMinusContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isUnaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(((UnaryExpression_minusContext) getExtendedContext(getContext().unaryExpression_minus()).getContext()).expression());}
	@Override
	public String getOperator() {
		return getContext().unaryExpression_minus().MINUS().getText().trim();
	}
	
	@Override
	public Type getType(){
		return getExtendedContext(((UnaryExpression_minusContext) getExtendedContext(getContext().unaryExpression_minus()).getContext()).expression()).getType();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(((UnaryExpression_minusContext) getExtendedContext(getContext().unaryExpression_minus()).getContext()).expression()).getTypeSize();
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(((UnaryExpression_minusContext) getExtendedContext(getContext().unaryExpression_minus()).getContext()).expression()).getTypeName();
	}
}
