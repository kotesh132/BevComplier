package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SelectCaseListContext;

public class SelectCaseListContextExt extends AbstractBaseExt {

	public SelectCaseListContextExt(SelectCaseListContext ctx) {
		super(ctx);
	}

	@Override
	public  SelectCaseListContext getContext(){
		return (SelectCaseListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SelectCaseListContext getContext(String str){
		return (SelectCaseListContext)new PopulateExtendedContextVisitor().visit(getParser(str).selectCaseList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SelectCaseListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SelectCaseListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
