package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.TypeArgContext;

public class TypeArgContextExt extends AbstractBaseExt {

	public TypeArgContextExt(TypeArgContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeArgContext getContext(){
		return (TypeArgContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeArgContext getContext(String str){
		return (TypeArgContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeArg());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeArgContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeArgContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(getContext().typeRef()).getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().typeRef()).getTypeSize();
	}
}
