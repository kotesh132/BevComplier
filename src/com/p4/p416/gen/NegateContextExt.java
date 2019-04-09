package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.NegateContext;
import com.p4.p416.gen.P416Parser.UnaryExpression_tildaContext;

public class NegateContextExt extends ExpressionContextExt {

	private static final Logger L = LoggerFactory.getLogger(NegateContextExt.class);

	public NegateContextExt(NegateContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  NegateContext getContext(){
		return (NegateContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NegateContext getContext(String str){
		return (NegateContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NegateContext){
				
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NegateContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isUnaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) ((UnaryExpression_tildaContext) getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression();}
	@Override
	public String getOperator() {
		return getContext().unaryExpression_tilda().TILDA().getText().trim();
	}
	
	@Override
	public Type getType(){
		if (!"BoolType".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_tildaContext)getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression())).getTypeName())
				&&
				!"BooleanLiteralTrue".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_tildaContext)getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression())).getTypeName())
				&&
				!"BooleanLiteralFalse".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_tildaContext)getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression())).getTypeName())){
		return this;
		}
		L.error("Line:"+getContext().start.getLine()+":  Negate can't be applied on boolean :"+getExtendedContext(getContext()).getFormattedText());
		return null;
	}
	
	@Override
	public int getTypeSize(){
		return ((ExpressionContextExt)getExtendedContext(((UnaryExpression_tildaContext)getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression())).getTypeSize();
	}
	
	@Override
	public String getTypeName(){
		//TODO It should be confirmed whether this will impact the semantic checks which are based on whether the resultant negate expression is signed or unsigned
		return ((ExpressionContextExt)getExtendedContext(((UnaryExpression_tildaContext)getExtendedContext(getContext().unaryExpression_tilda()).getContext()).expression())).getTypeName();
	}
}
