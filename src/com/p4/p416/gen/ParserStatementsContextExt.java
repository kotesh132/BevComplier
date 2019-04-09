package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ParserStatementsContext;

public class ParserStatementsContextExt extends AbstractBaseExt {

	public ParserStatementsContextExt(ParserStatementsContext ctx) {
		super(ctx);
	}

	@Override
	public  ParserStatementsContext getContext(){
		return (ParserStatementsContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserStatementsContext getContext(String str){
		return (ParserStatementsContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserStatements());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserStatementsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserStatementsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

}
