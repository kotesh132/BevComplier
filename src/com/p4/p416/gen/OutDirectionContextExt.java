package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OutDirectionContext;

public class OutDirectionContextExt extends AbstractBaseExt {

	public OutDirectionContextExt(OutDirectionContext ctx) {
		super(ctx);
	}

	@Override
	public  OutDirectionContext getContext(){
		return (OutDirectionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OutDirectionContext getContext(String str){
		return (OutDirectionContext)new PopulateExtendedContextVisitor().visit(getParser(str).direction());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OutDirectionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OutDirectionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
