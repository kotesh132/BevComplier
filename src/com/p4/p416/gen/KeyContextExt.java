package com.p4.p416.gen;

import lombok.Data;
import lombok.EqualsAndHashCode;


import  org.antlr.v4.runtime.ParserRuleContext;

import  com.p4.p416.gen.P416Parser.*;

@EqualsAndHashCode(callSuper=false)
@Data
public class KeyContextExt extends TablePropertyContextExt {

	protected Integer sram;
	protected Integer tcam;

	public KeyContextExt(KeyContext ctx) {
		super(ctx);
	}

	@Override
	public  KeyContext getContext(){
		return (KeyContext)contexts.get(contexts.size()-1);
	}

	@Override
	public KeyContext getContext(String str){
		return (KeyContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableProperty());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof KeyContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ KeyContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
}
