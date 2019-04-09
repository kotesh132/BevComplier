package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class EntryContextExt extends AbstractBaseExt {

	public EntryContextExt(EntryContext ctx) {
		super(ctx);
	}

	@Override
	public  EntryContext getContext(){
		return (EntryContext)contexts.get(contexts.size()-1);
	}

	@Override
	public EntryContext getContext(String str){
		return (EntryContext)new PopulateExtendedContextVisitor().visit(getParser(str).entry());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof EntryContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ EntryContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
