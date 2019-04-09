package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class LocalConstVariableContextExt extends TablePropertyContextExt {

	public LocalConstVariableContextExt(LocalConstVariableContext ctx) {
		super(ctx);
	}

	@Override
	public  LocalConstVariableContext getContext(){
		return (LocalConstVariableContext)contexts.get(contexts.size()-1);
	}

	@Override
	public LocalConstVariableContext getContext(String str){
		return (LocalConstVariableContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableProperty());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof LocalConstVariableContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ LocalConstVariableContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
