package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OctalNumberContext;

public class OctalNumberContextExt extends AbstractBaseExt {

	public OctalNumberContextExt(OctalNumberContext ctx) {
		super(ctx);
	}

	@Override
	public  OctalNumberContext getContext(){
		return (OctalNumberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OctalNumberContext getContext(String str){
		return (OctalNumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).octalNumber());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OctalNumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OctalNumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
