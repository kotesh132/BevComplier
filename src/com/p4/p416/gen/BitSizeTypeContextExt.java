package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.p4.p416.gen.P416Parser.*;
import com.p4.utils.Utils;

import lombok.Getter;

import com.p4.p416.applications.Type;

public class BitSizeTypeContextExt extends BaseTypeContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(BitSizeTypeContextExt.class);

	@Getter
	private BitSizeTypeContext ctx;

	public BitSizeTypeContextExt(BitSizeTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  BitSizeTypeContext getContext(){
		return (BitSizeTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public BitSizeTypeContext getContext(String str){
		return (BitSizeTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).baseType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BitSizeTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ BitSizeTypeContext.class.getName());
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
		return "BitSizeType";
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
			if( type instanceof BitSizeTypeContextExt )
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
		BitSizeTypeContext ctx = getContext();
		return getExtendedContext(ctx.number()).getFormattedText();
	}
	
	@Override
	public int getTypeSize(){
		int size = 0;
		try{
			size =  getSizeInBits();
		}
		catch(NumberFormatException e){
			L.error("Line:"+getContext().start.getLine()+": Invalid Width: "+e.getMessage());
		}
		return size;
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
