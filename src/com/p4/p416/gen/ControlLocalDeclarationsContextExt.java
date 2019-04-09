package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class ControlLocalDeclarationsContextExt extends AbstractBaseExt {

	public ControlLocalDeclarationsContextExt(ControlLocalDeclarationsContext ctx) {
		super(ctx);
	}

	@Override
	public  ControlLocalDeclarationsContext getContext(){
		return (ControlLocalDeclarationsContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ControlLocalDeclarationsContext getContext(String str){
		return (ControlLocalDeclarationsContext)new PopulateExtendedContextVisitor().visit(getParser(str).controlLocalDeclarations());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ControlLocalDeclarationsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ControlLocalDeclarationsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
