package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class MethodPrototypesContextExt extends AbstractBaseExt {

	public MethodPrototypesContextExt(MethodPrototypesContext ctx) {
		super(ctx);
	}

	@Override
	public  MethodPrototypesContext getContext(){
		return (MethodPrototypesContext)contexts.get(contexts.size()-1);
	}

	@Override
	public MethodPrototypesContext getContext(String str){
		return (MethodPrototypesContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodPrototypes());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MethodPrototypesContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MethodPrototypesContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
