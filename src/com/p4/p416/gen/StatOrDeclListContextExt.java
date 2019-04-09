package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.StatOrDeclListContext;

public class StatOrDeclListContextExt extends AbstractBaseExt {

	public StatOrDeclListContextExt(StatOrDeclListContext ctx) {
		super(ctx);
	}

	@Override
	public  StatOrDeclListContext getContext(){
		return (StatOrDeclListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StatOrDeclListContext getContext(String str){
		return (StatOrDeclListContext)new PopulateExtendedContextVisitor().visit(getParser(str).statOrDeclList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StatOrDeclListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StatOrDeclListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
