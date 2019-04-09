package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class EntriesListContextExt extends AbstractBaseExt {

	public EntriesListContextExt(EntriesListContext ctx) {
		super(ctx);
	}

	@Override
	public  EntriesListContext getContext(){
		return (EntriesListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public EntriesListContext getContext(String str){
		return (EntriesListContext)new PopulateExtendedContextVisitor().visit(getParser(str).entriesList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof EntriesListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ EntriesListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
