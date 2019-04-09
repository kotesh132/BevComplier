package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ParserBlockStatementContext;

public class ParserBlockStatementContextExt extends AbstractBaseExt {

	public ParserBlockStatementContextExt(ParserBlockStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  ParserBlockStatementContext getContext(){
		return (ParserBlockStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserBlockStatementContext getContext(String str){
		return (ParserBlockStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserBlockStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserBlockStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserBlockStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
