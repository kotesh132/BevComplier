package com.p4.p416.gen;


import java.util.ArrayList;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ActionListContext;
import com.p4.p416.gen.P416Parser.ActionRefContext;
import com.p4.p416.gen.P416Parser.ActionsContext;



public class ActionsContextExt extends TablePropertyContextExt {

	public ActionsContextExt(ActionsContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  ActionsContext getContext(){
		return (ActionsContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActionsContext getContext(String str){
		return (ActionsContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableProperty());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ActionsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ActionsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	private List<ActionRefContextExt> actionRefContextExts;
	public List<ActionRefContextExt> getActionList() {
		if (actionRefContextExts != null && actionRefContextExts.size() > 0) 
			return actionRefContextExts;
		else{
			actionRefContextExts = new ArrayList<ActionRefContextExt>();
			ActionsContext actionsContext = getContext();
			ActionListContext actionListContext = actionsContext.actionList();
			if(actionListContext != null && !getExtendedContext(actionListContext).getFormattedText().isEmpty()) {
				for(ActionRefContext actionRefContext: actionListContext.actionRef())
					actionRefContextExts.add((ActionRefContextExt) getExtendedContext(actionRefContext));
			}
			return actionRefContextExts;
		}
	}
}
