package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ModContext;

public class ModContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(ModContextExt.class);

	public ModContextExt(ModContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ModContext getContext(){
		return (ModContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModContext getContext(String str){
		return (ModContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ModContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ModContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext((ModContext)getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext((ModContext)getContext().expression(1));}
	@Override
	public String getOperator() {
		return getContext().PRCNT().getText().trim();
	}
	
	@Override 
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((ModContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((ModContext)getContext()).expression(1));
		Type leftType = leftExpression.getType();
		Type rightType = rightExpression.getType();
		
		if (leftType==null || rightType==null){
			return null;
		}
		
		//TODO: Expression evaluation in case rightExpression is of expression kind
		if ("0".equals(rightExpression.getFormattedText())){
            L.error("Line:"+getContext().start.getLine()+": "+"Modulo can't be done by zero!");
		}
		
		//TODO: Expression evaluation should be done here too. Till then using numeric exceptions to avoid inferring expressions 
		try{
			int left = Integer.parseInt(leftExpression.getFormattedText());
			int right = Integer.parseInt(rightExpression.getFormattedText());
			if (left<0 || right<0){
				L.error("Line:"+getContext().start.getLine()+": "+"Modulo is not defined for negative numbers");
			}
		}catch(NumberFormatException e){
		}
		return leftType;
	}
}
