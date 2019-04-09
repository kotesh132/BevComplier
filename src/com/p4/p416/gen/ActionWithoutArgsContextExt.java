package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.cfg.TableMeta;
import com.p4.p416.gen.P416Parser.ActionWithoutArgsContext;

public class ActionWithoutArgsContextExt extends ActionRefContextExt{

	public ActionWithoutArgsContextExt( ActionWithoutArgsContext ctx) {
		super(ctx);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public  ActionWithoutArgsContext getContext(){
		return (ActionWithoutArgsContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActionWithoutArgsContext getContext(String str){
		return (ActionWithoutArgsContext)new PopulateExtendedContextVisitor().visit(getParser(str).actionRef());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ActionWithoutArgsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ActionWithoutArgsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public String getActionName()
	{
		return getExtendedContext(getContext().name()).getFormattedText();
	}

	@Override
	public boolean isDefaultAction() {
		return getExtendedContext(getContext()).getFormattedText().startsWith("@default_only");
	}
	
	@Override
	public void populateActionsList(TableMeta t){
		t.addAction(getExtendedContext(getContext().name()).getFormattedText());
	}
}
