package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.TypeArgumentListContext;

public class TypeArgumentListContextExt extends AbstractBaseExt {

	public TypeArgumentListContextExt(TypeArgumentListContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeArgumentListContext getContext(){
		return (TypeArgumentListContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeArgumentListContext getContext(String str){
		return (TypeArgumentListContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeArgumentList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeArgumentListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeArgumentListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
