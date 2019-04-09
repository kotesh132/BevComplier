package com.p4.p416.gen;

import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.IRangeIndex;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.RangeIndexContext;

public class RangeIndexContextExt extends ExpressionContextExt implements IRangeIndex {

	public RangeIndexContextExt(RangeIndexContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  RangeIndexContext getContext(){
		return (RangeIndexContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RangeIndexContext getContext(String str){
		return (RangeIndexContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof RangeIndexContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ RangeIndexContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public String getNameString() {
		return getExpression().getFormattedText();
	}

	@Override
	public boolean isRangeIndexExpression() {
		return true;
	}

	@Override
	public IExpression getFrom() {
		return (IExpression) getExtendedContext(getContext().expression(2));
	}

	@Override
	public IExpression getTo() {
		return (IExpression) getExtendedContext(getContext().expression(1));
	}

	@Override
	public IExpression getExpression() {
		return (IExpression) getExtendedContext(getContext().expression(0));
	}
	
	@Override
	public Type getType(){
		//return this.getContext().expression(0).extendedContext.getType();
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(getContext().expression(0)).getTypeName();
		//return getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		//Expression evaluation needs to be integrated. This will work currently for Range Indexes of Type 	dt[7:0] where expression(1) and expression(2) are of Basic Types
		return getExtendedContext(getContext().expression(1)).getTypeSize() - getExtendedContext(getContext().expression(2)).getTypeSize()+1;
	}
}
