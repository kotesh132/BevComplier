package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class InOutDirectionContextExt extends AbstractBaseExt {

	public InOutDirectionContextExt(InOutDirectionContext ctx) {
		super(ctx);
	}

	@Override
	public  InOutDirectionContext getContext(){
		return (InOutDirectionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public InOutDirectionContext getContext(String str){
		return (InOutDirectionContext)new PopulateExtendedContextVisitor().visit(getParser(str).direction());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof InOutDirectionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ InOutDirectionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
