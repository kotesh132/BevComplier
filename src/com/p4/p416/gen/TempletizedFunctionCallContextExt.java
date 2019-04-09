package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.FunctionCallContext;
import com.p4.p416.gen.P416Parser.TempletizedFunctionCallContext;

public class TempletizedFunctionCallContextExt extends ExpressionContextExt {

	public TempletizedFunctionCallContextExt(TempletizedFunctionCallContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TempletizedFunctionCallContext getContext(){
		return (TempletizedFunctionCallContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TempletizedFunctionCallContext getContext(String str){
		return (TempletizedFunctionCallContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TempletizedFunctionCallContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TempletizedFunctionCallContext.class.getName());
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
		return getExtendedContext(getContext().expression()).getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().expression()).getTypeSize();
	}
	
	@Override
	public String getSymbolName(){
		return getExtendedContext(((TempletizedFunctionCallContext)getContext()).expression()).getFormattedText();
	}
	
}
