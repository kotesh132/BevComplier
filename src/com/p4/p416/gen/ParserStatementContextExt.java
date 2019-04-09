package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ParserStatementContext;

public class ParserStatementContextExt extends AbstractBaseExt {

	public ParserStatementContextExt(ParserStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  ParserStatementContext getContext(){
		return (ParserStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserStatementContext getContext(String str){
		return (ParserStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
