package com.p4.p416.gen;

import java.util.List;



import  org.antlr.v4.runtime.ParserRuleContext;

import  com.p4.p416.gen.P416Parser.*;

public class ControlLocalDeclarationContextExt extends AbstractBaseExt {

	public ControlLocalDeclarationContextExt(ControlLocalDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  ControlLocalDeclarationContext getContext(){
		return (ControlLocalDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ControlLocalDeclarationContext getContext(String str){
		return (ControlLocalDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).controlLocalDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ControlLocalDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ControlLocalDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	//POOJA
	@Override
	public void getControlLocalDeclarationsContextExt(List<ControlLocalDeclarationContextExt> controlLocalDeclarations){
		controlLocalDeclarations.add(this);
	}
}
