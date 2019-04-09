package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class LocalVariableContextExt extends TablePropertyContextExt {

	public LocalVariableContextExt(LocalVariableContext ctx) {
		super(ctx);
	}

	@Override
	public  LocalVariableContext getContext(){
		return (LocalVariableContext)contexts.get(contexts.size()-1);
	}

	@Override
	public LocalVariableContext getContext(String str){
		return (LocalVariableContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableProperty());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof LocalVariableContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ LocalVariableContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
