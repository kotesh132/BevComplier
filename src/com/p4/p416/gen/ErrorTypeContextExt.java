package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class ErrorTypeContextExt extends BaseTypeContextExt {

	public ErrorTypeContextExt(ErrorTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  ErrorTypeContext getContext(){
		return (ErrorTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ErrorTypeContext getContext(String str){
		return (ErrorTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ErrorTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ErrorTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public String getTypeName()
	{
		return "ErrorType";
	}
	
	@Override
	public int getTypeSize(){
		return this.getSizeInBits();
	}

	@Override 
	public Type getType1()
	{
		return this;
	}

	@Override 
	public Type getType()
	{
		return this;
	}

	@Override
	public Boolean isSame(Type type)
	{
		return type.getTypeName().equals(getTypeName());
	}

	@Override
	public int getSizeInBits()
	{
		return 0;
	}

	@Override
	public int getSizeInBytes()
	{
		return 0;
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		return 0;
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/
	
}
