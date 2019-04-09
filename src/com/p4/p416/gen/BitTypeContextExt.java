package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class BitTypeContextExt extends BaseTypeContextExt {

	public BitTypeContextExt(BitTypeContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  BitTypeContext getContext(){
		return (BitTypeContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitTypeContext getContext(String str){
		return (BitTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BitTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ BitTypeContext.class.getName());
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
		return "BitType";
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

	@Override
	public MemoryType getMemoryType()
	{
		return MemoryType.TypePktBit;
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public boolean isEquivalenceCompatible(Type that){
		if ("BoolType".equals(that.getTypeName())
				|| "BooleanLiteralTrue".equals(that.getTypeName())
					|| "BooleanLiteralFalse".equals(that.getTypeName())
						|| "BitType".equals(that.getTypeName())
							|| ("BitSizeType".equals(that.getTypeName()) && that.getTypeSize() == 1)
							   || ("IntegerLiteral".equals(that.getTypeName())   &&    ("1".equals(((IntegerContextExt)that).getFormattedText())||"0".equals(((IntegerContextExt)that).getFormattedText())))){
			//TODO: Expression evaluation in above "IntegerLiteral case
			return true;
		}
		return false;
	}

}
