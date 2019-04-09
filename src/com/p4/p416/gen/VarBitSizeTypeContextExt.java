package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.NumberContext;
import com.p4.p416.gen.P416Parser.VarBitSizeTypeContext;
import com.p4.utils.Utils;

public class VarBitSizeTypeContextExt extends BaseTypeContextExt {

	public static final int SIZE_INDETERMINATE = -1;

	public VarBitSizeTypeContextExt(VarBitSizeTypeContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  VarBitSizeTypeContext getContext(){
		return (VarBitSizeTypeContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public VarBitSizeTypeContext getContext(String str){
		return (VarBitSizeTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof VarBitSizeTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ VarBitSizeTypeContext.class.getName());
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
		return "VarBitSizeType";
	}
	
	@Override
	public int getTypeSize(){
		return VarBitSizeTypeContextExt.SIZE_INDETERMINATE;
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
			if( type instanceof VarBitSizeTypeContextExt )
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
		int sizeinBits =  Integer.parseInt(getExtendedContext(numberContext).getFormattedText());
		return sizeinBits;
	}
	
@Override
	public int getSizeInBytes()
	{
		return Utils.ceil(getSizeInBits() ,8);
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		return Utils.ceil(getSizeInBits() ,8);
	}


	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/


	//GL - ControlBlock END

	@Override
	protected String getLookaheadValue(){
		VarBitSizeTypeContext ctx = getContext();
		return getExtendedContext(ctx.number()).getFormattedText();
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type type){
		VarBitSizeTypeContextExt operand1 = this;
		VarBitSizeTypeContextExt operand2 = (VarBitSizeTypeContextExt)type;
		
		if (operand1.getTypeSize() == operand2.getTypeSize() && operand1.getTypeName() == operand2.getTypeName()){
			return true;
		}
		
		return false;
	}
}
