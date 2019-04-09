package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class ArgumentListContextExt extends AbstractBaseExt {

	public ArgumentListContextExt(ArgumentListContext ctx) {
		super(ctx);
	}

	@Override
	public  ArgumentListContext getContext(){
		return (ArgumentListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ArgumentListContext getContext(String str){
		return (ArgumentListContext)new PopulateExtendedContextVisitor().visit(getParser(str).argumentList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ArgumentListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ArgumentListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
