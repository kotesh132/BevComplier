package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.MemberAccessContext;

public class MemberAccessContextExt extends ExpressionContextExt {

	public MemberAccessContextExt(MemberAccessContext ctx) {
		super(ctx);
	}

	@Override
	public  MemberAccessContext getContext(){
		return (MemberAccessContext)contexts.get(contexts.size()-1);
	}

	@Override
	public MemberAccessContext getContext(String str){
		return (MemberAccessContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MemberAccessContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MemberAccessContext.class.getName());
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
