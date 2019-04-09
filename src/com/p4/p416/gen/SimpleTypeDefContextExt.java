package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SimpleTypeDefContext;

public class SimpleTypeDefContextExt extends AbstractBaseExt {

	public SimpleTypeDefContextExt(SimpleTypeDefContext ctx) {
		super(ctx);
	}

	@Override
	public  SimpleTypeDefContext getContext(){
		return (SimpleTypeDefContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SimpleTypeDefContext getContext(String str){
		return (SimpleTypeDefContext)new PopulateExtendedContextVisitor().visit(getParser(str).typedefDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SimpleTypeDefContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SimpleTypeDefContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
