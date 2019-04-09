package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ArrayIndexContext;

public class ArrayIndexContextExt extends ExpressionContextExt {

	public ArrayIndexContextExt(ArrayIndexContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ArrayIndexContext getContext(){
		return (ArrayIndexContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayIndexContext getContext(String str){
		return (ArrayIndexContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ArrayIndexContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ArrayIndexContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(getContext().expression(0)).getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().expression(1)).getTypeSize();
	}
}
