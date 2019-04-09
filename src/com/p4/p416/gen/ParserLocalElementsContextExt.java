package com.p4.p416.gen;


import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ParserLocalElementsContext;



public class ParserLocalElementsContextExt extends AbstractBaseExt {

	

	public ParserLocalElementsContextExt(ParserLocalElementsContext ctx) {
		super(ctx);

	}

	@Override
	public  ParserLocalElementsContext getContext(){
		return (ParserLocalElementsContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserLocalElementsContext getContext(String str){
		return (ParserLocalElementsContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserLocalElements());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserLocalElementsContext){
				
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserLocalElementsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
