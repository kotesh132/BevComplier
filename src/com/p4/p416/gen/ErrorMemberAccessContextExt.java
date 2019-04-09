package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ErrorMemberAccessContext;

public class ErrorMemberAccessContextExt extends ExpressionContextExt {

	public ErrorMemberAccessContextExt(ErrorMemberAccessContext ctx) {
		super(ctx);
	}

	@Override
	public  ErrorMemberAccessContext getContext(){
		return (ErrorMemberAccessContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ErrorMemberAccessContext getContext(String str){
		return (ErrorMemberAccessContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ErrorMemberAccessContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ErrorMemberAccessContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public boolean isTerminal() {
		//TODO this is not true & is a shortcut
		return true;
	}
}
