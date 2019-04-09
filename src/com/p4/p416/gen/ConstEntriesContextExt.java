package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class ConstEntriesContextExt extends TablePropertyContextExt {

	public ConstEntriesContextExt(ConstEntriesContext ctx) {
		super(ctx);
	}

	@Override
	public  ConstEntriesContext getContext(){
		return (ConstEntriesContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ConstEntriesContext getContext(String str){
		return (ConstEntriesContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableProperty());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ConstEntriesContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ConstEntriesContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
