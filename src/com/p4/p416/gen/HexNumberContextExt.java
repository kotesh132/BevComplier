package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class HexNumberContextExt extends AbstractBaseExt {

	public HexNumberContextExt(HexNumberContext ctx) {
		super(ctx);
	}

	@Override
	public  HexNumberContext getContext(){
		return (HexNumberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public HexNumberContext getContext(String str){
		return (HexNumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).hexNumber());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof HexNumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ HexNumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
