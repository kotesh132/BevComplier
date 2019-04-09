package com.p4.p416.gen;


import com.p4.p416.gen.P416Parser.ActionListContext;


import  org.antlr.v4.runtime.ParserRuleContext;

public class ActionListContextExt extends AbstractBaseExt {

	
	public ActionListContextExt(ActionListContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  ActionListContext getContext(){
		return (ActionListContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActionListContext getContext(String str){
		return (ActionListContext)new PopulateExtendedContextVisitor().visit(getParser(str).actionList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ActionListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ActionListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

}
