package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SwitchCasesContext;

public class SwitchCasesContextExt extends AbstractBaseExt {

	public SwitchCasesContextExt(SwitchCasesContext ctx) {
		super(ctx);
	}

	@Override
	public  SwitchCasesContext getContext(){
		return (SwitchCasesContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SwitchCasesContext getContext(String str){
		return (SwitchCasesContext)new PopulateExtendedContextVisitor().visit(getParser(str).switchCases());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SwitchCasesContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SwitchCasesContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
}
