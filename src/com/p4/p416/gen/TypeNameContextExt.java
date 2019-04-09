package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.TypeNameContext;

public class TypeNameContextExt extends AbstractBaseExt {

	public TypeNameContextExt(TypeNameContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeNameContext getContext(){
		return (TypeNameContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeNameContext getContext(String str){
		return (TypeNameContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeName());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeNameContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeNameContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//Control Block START
	//GL - ControlBlock START
	/* Define a symbol in the parent's scope */

	@Override
	public String getTypeName()
	{
		return getFormattedText();
	}



}
