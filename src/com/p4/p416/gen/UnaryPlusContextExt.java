package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.UnaryExpression_plusContext;
import com.p4.p416.gen.P416Parser.UnaryPlusContext;

public class UnaryPlusContextExt extends ExpressionContextExt {

	public UnaryPlusContextExt(UnaryPlusContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  UnaryPlusContext getContext(){
		return (UnaryPlusContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public UnaryPlusContext getContext(String str){
		return (UnaryPlusContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof UnaryPlusContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryPlusContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(((UnaryExpression_plusContext) getExtendedContext(getContext().unaryExpression_plus()).getContext()).expression());}
	@Override public boolean isUnaryExpression(){return true;}
	@Override
	public String getOperator() {
		return getContext().unaryExpression_plus().PLUS().getText().trim();
	}
	
	@Override
	public Type getType(){
		return getExtendedContext(((UnaryExpression_plusContext) getExtendedContext(getContext().unaryExpression_plus()).getContext()).expression()).getType();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(((UnaryExpression_plusContext) getExtendedContext(getContext().unaryExpression_plus()).getContext()).expression()).getTypeSize();
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(((UnaryExpression_plusContext) getExtendedContext(getContext().unaryExpression_plus()).getContext()).expression()).getTypeName();
	}
}
