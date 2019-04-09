package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class BoolTypeContextExt extends BaseTypeContextExt {

	public BoolTypeContextExt(BoolTypeContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  BoolTypeContext getContext(){
		return (BoolTypeContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BoolTypeContext getContext(String str){
		return (BoolTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BoolTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ BoolTypeContext.class.getName());
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
		return "BoolType";
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
		return 1;
	}

	@Override
	public int getSizeInBytes()
	{
		return 1;
	}
	@Override
	public int getAlignedSizeInBytes()
	{
		return 1;
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public boolean isEquivalenceCompatible(Type that){
		if ("BoolType".equals(that.getTypeName())
				|| "BooleanLiteralTrue".equals(that.getTypeName())
						|| "BooleanLiteralFalse".equals(that.getTypeName())){
			return true;
		}
		return false;
	}



}
