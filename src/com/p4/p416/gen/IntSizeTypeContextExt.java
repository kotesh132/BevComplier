package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.utils.Utils;
import com.p4.p416.applications.Type;

public class IntSizeTypeContextExt extends BaseTypeContextExt {

	public IntSizeTypeContextExt(IntSizeTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  IntSizeTypeContext getContext(){
		return (IntSizeTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public IntSizeTypeContext getContext(String str){
		return (IntSizeTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof IntSizeTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ IntSizeTypeContext.class.getName());
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
		return "IntSizeType";
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
		if ( type.getTypeName().equals(getTypeName()))
		{
			if( type instanceof IntSizeTypeContextExt )
			{
				//TODO:GL we may have to eval the number before comparing.
				NumberContext thatNumberContext  = getExtendedContext(((BitSizeTypeContextExt)type).getContext().number()).getContext();
				NumberContext thisNumberContext = getExtendedContext(getContext().number()).getContext();
				if(getExtendedContext(thatNumberContext).getFormattedText().equals(getExtendedContext(thisNumberContext).getFormattedText()))
				{
					return true;
				}
				
			}
		}
		return false;
	}
	
	@Override
	public int getSizeInBits()
	{
		NumberContext numberContext = getExtendedContext(getContext().number()).getContext();
		try {
			int sizeinBits =  Integer.parseInt(getExtendedContext(numberContext).getFormattedText());
			return sizeinBits;
		} catch(NumberFormatException numberFormatException) {
			L.error("Not in a proper number format");
		}
		return -1;
	}
	
	@Override
	public int getAlignedSizeInBytes()
	{
		return Utils.ceil(getSizeInBits() ,8);
	}
	@Override
	public int getSizeInBytes()
	{
		return Utils.ceil(getSizeInBits() ,8);
	}
	//GL - ControlBlock END

	@Override
	protected String getLookaheadValue(){
		IntSizeTypeContext ctx = getContext();
		return getExtendedContext(ctx.number()).getFormattedText();
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type that){
		Type operand1 = this;
		Type operand2 = that;
		
		if (operand1.getTypeSize() == operand2.getTypeSize() && operand1.getTypeName() == operand2.getTypeName()){
			return true;
		}
		
		if ("IntegerLiteral".equals(operand2.getTypeName())){
			return true;
		}
		
		return false;
	}
	
}
