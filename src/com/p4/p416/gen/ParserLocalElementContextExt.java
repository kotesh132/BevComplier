package com.p4.p416.gen;


import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ParserLocalElementContext;



public class ParserLocalElementContextExt extends AbstractBaseExt {

	

	public ParserLocalElementContextExt(ParserLocalElementContext ctx) {
		super(ctx);

	}

	@Override
	public  ParserLocalElementContext getContext(){
		return (ParserLocalElementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserLocalElementContext getContext(String str){
		return (ParserLocalElementContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserLocalElement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserLocalElementContext){
				
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserLocalElementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
