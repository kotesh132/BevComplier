package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SpecializedTypeContext;

public class SpecializedTypeContextExt extends AbstractBaseExt {

	public SpecializedTypeContextExt(SpecializedTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  SpecializedTypeContext getContext(){
		return (SpecializedTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SpecializedTypeContext getContext(String str){
		return (SpecializedTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).specializedType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SpecializedTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SpecializedTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
