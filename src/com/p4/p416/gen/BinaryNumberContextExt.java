package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;

public class BinaryNumberContextExt extends AbstractBaseExt {

	public BinaryNumberContextExt(BinaryNumberContext ctx) {
		super(ctx);
	}

	@Override
	public  BinaryNumberContext getContext(){
		return (BinaryNumberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public BinaryNumberContext getContext(String str){
		return (BinaryNumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).binaryNumber());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BinaryNumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ BinaryNumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
