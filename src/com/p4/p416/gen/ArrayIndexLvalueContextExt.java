package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class ArrayIndexLvalueContextExt extends LvalueContextExt {

	public ArrayIndexLvalueContextExt(ArrayIndexLvalueContext ctx) {
		super(ctx);
	}

	@Override
	public  ArrayIndexLvalueContext getContext(){
		return (ArrayIndexLvalueContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ArrayIndexLvalueContext getContext(String str){
		return (ArrayIndexLvalueContext)new PopulateExtendedContextVisitor().visit(getParser(str).lvalue());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ArrayIndexLvalueContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ArrayIndexLvalueContext.class.getName());
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
		throw new UnsupportedOperationException("getTypeName");
	}
	
	@Override
	public Boolean isSame(Type thatType)
	{
		throw new UnsupportedOperationException("isSame");
	}

	@Override
	public int getSizeInBits()
	{
		throw new UnsupportedOperationException("isSame");
	}
	
	@Override
	public int getSizeInBytes()
	{
		return getSizeInBytes(getSymbolName());
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		return getAlignedSizeInBytes(getSymbolName());
	}
	
	@Override
	public int getBitOffset()
	{
		throw new UnsupportedOperationException("getBitOffset");
	}

	@Override
	public int getByteOffset()
	{
		throw new UnsupportedOperationException("getByteOffset");
	}

	@Override
	public int getAlignedByteOffset()
	{
		throw new UnsupportedOperationException("getAlignedByteOffset");
	}
	
	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/
}
