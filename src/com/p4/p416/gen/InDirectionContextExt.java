package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class InDirectionContextExt extends AbstractBaseExt {

	public InDirectionContextExt(InDirectionContext ctx) {
		super(ctx);
	}

	@Override
	public  InDirectionContext getContext(){
		return (InDirectionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public InDirectionContext getContext(String str){
		return (InDirectionContext)new PopulateExtendedContextVisitor().visit(getParser(str).direction());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof InDirectionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ InDirectionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
