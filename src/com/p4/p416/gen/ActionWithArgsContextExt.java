package com.p4.p416.gen;


import com.p4.p416.iface.IArgument;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.cfg.TableMeta;
import com.p4.p416.gen.P416Parser.ActionWithArgsContext;


import java.util.ArrayList;
import java.util.List;

public class ActionWithArgsContextExt extends  ActionRefContextExt{

	public ActionWithArgsContextExt(ActionWithArgsContext ctx) {
		super(ctx);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public  ActionWithArgsContext getContext(){
		return (ActionWithArgsContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActionWithArgsContext getContext(String str){
		return (ActionWithArgsContext)new PopulateExtendedContextVisitor().visit(getParser(str).actionRef());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ActionWithArgsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ActionWithArgsContext.class.getName());
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

	@Override
	public List<IArgument> getArguments() {
		List<IArgument> arguments = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IArgument) {
				arguments.add((IArgument) node);
			}
		});
		return arguments;
	}
}
