package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.PrefixedNonTypeNameContext;

public class PrefixedNonTypeNameContextExt extends AbstractBaseExt {

	public PrefixedNonTypeNameContextExt(PrefixedNonTypeNameContext ctx) {
		super(ctx);
	}

	@Override
	public  PrefixedNonTypeNameContext getContext(){
		return (PrefixedNonTypeNameContext)contexts.get(contexts.size()-1);
	}

	@Override
	public PrefixedNonTypeNameContext getContext(String str){
		return (PrefixedNonTypeNameContext)new PopulateExtendedContextVisitor().visit(getParser(str).prefixedNonTypeName());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PrefixedNonTypeNameContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PrefixedNonTypeNameContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
