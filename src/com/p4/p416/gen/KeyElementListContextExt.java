package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class KeyElementListContextExt extends AbstractBaseExt {

	public KeyElementListContextExt(KeyElementListContext ctx) {
		super(ctx);
	}

	@Override
	public  KeyElementListContext getContext(){
		return (KeyElementListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public KeyElementListContext getContext(String str){
		return (KeyElementListContext)new PopulateExtendedContextVisitor().visit(getParser(str).keyElementList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof KeyElementListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ KeyElementListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
