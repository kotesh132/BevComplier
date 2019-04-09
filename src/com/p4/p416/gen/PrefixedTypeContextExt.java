package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.PrefixedTypeContext;

public class PrefixedTypeContextExt extends AbstractBaseExt {

	public PrefixedTypeContextExt(PrefixedTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  PrefixedTypeContext getContext(){
		return (PrefixedTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public PrefixedTypeContext getContext(String str){
		return (PrefixedTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).prefixedType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PrefixedTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PrefixedTypeContext.class.getName());
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
		PrefixedTypeContext ctx = getContext();
		if (ctx.dotPrefix() != null || ctx.ERROR() != null )
			throw new UnsupportedOperationException("getTypeName()");

		return ctx.IDENTIFIER().getText();
	}

	@Override 
	public Type getType1()
	{
		return getType();
	}

	@Override 
	public Type getType()
	{
		String typeName = getTypeName();
		return  (Type)resolve(typeName);
	}

	@Override
	public Boolean isSame(Type type)
	{
		return type.getTypeName().equals(getTypeName());
	}

	@Override
	public int getSizeInBits()
	{
		Type type = getType();
		if( type != null){
			return type.getSizeInBits();
		}
		else{
			throw new SymbolNotDefinedException(getTypeName());
		}
	}

	@Override
	public int getSizeInBytes()
	{
		Type type = getType1();
		if( type != null){
			return type.getSizeInBytes();
		}
		else{
			throw new SymbolNotDefinedException(getTypeName());
		}
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		Type type = getType1();
		if( type != null){
			return type.getAlignedSizeInBytes();
		}
		else{
			throw new SymbolNotDefinedException(getTypeName());
		}
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public Boolean hasPrefixedType(){
		return true;
	}

	@Override
	protected String getPrefixedType(){
		return getContext().getText();
	}

}
