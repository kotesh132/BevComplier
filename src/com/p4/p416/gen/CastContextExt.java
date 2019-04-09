package com.p4.p416.gen;

import com.p4.p416.gen.P416Parser.ExpressionContext;
import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.CastContext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

public class CastContextExt extends ExpressionContextExt {

	private static final Logger L = LoggerFactory.getLogger(CastContextExt.class);
	
	private String thisCastType;
	private int thisCastSize;

	public CastContextExt(CastContext ctx) {
		super(ctx);
	}

	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public CastContext getContext(String str){
		return (CastContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof CastContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ CastContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		if (getContext() instanceof CastContext) {
			String formattedText = getExtendedContext(((CastContext) getContext()).expression()).getFormattedText();
			P416Parser parser = getParser(formattedText);
			P416Parser.ExpressionContext expressionContext = (ExpressionContext) new PopulateExtendedContextVisitor().visit(parser.expression());
			this.setContext(expressionContext);
		}
	}
	
	@Override
	public Type getType(){
		Type castType = getExtendedContext((((CastContext)getContext())).typeRef()).getType();
		Type expressionType = getExtendedContext((((CastContext)getContext())).expression()).getType();
		
		if (castType==null || expressionType==null){
			return null;
		}
		
		//checking compile time compatibility of type cast
		if (!this.isTypeCompatible(castType,expressionType)){
	        L.error("Line:"+getContext().start.getLine()+": '"+getExtendedContext(((CastContext)getContext()).expression()).getFormattedText()+"' can not be cast to '"+ castType.getTypeName()+"'");
			return null;
		}
		else{
			thisCastType = castType.getTypeName();
			thisCastSize = castType.getTypeSize();
		}
		return this;
	}
	
	@Override
	public boolean isTypeCompatible(Type type1, Type type2){
		String typeName1 = type1.getTypeName();
		String typeName2 = type2.getTypeName();
		int typeWidth1 = type1.getTypeSize();
		int typeWidth2 = type2.getTypeSize();
		
		
		if ("BoolType".equals(typeName1)){
			
			/*
			 * 
			 * Handling explicit cast of IntegerLiteral say 1 to bool
			 */
			if ("IntegerLiteral".equals(typeName2)){
				return true;
			}
			
			/*
			 * handling explicit cast of BitSizeType of width 1 to bool
			e.g., following should be valid
			
			bool a; 
			bit<1> b; 
			a = (bool)b;
			
			*
			*/
			if ("BitSizeType".equals(typeName2)){
				if (typeWidth2==1){
					return true;
				}
			}
			
			/*
			 * handling explicit cast of BitType to bool
			e.g., following should be valid
			
			bool a; 
			bit b; 
			a = (bool)b;
			
			*
			*/
			if (("BitType".equals(typeName2))){
				return true;
			}
			
			/*
			 * handling explicit cast of bool to bool
			e.g., following should be valid (though may not be usually used)
			
			bool a; 
			bool b; 
			a = (bool)b;
			
			*
			*/
			if ("BoolType".equals(typeName2)){
				return true;
			}
		}
		
		if ("IntSizeType".equals(typeName1)){
			
			/*
			 * 
			 * Handling explicit cast of IntegerLiteral say 888 to int<w>
			 */
			if ("IntegerLiteral".equals(typeName2)){
				return true;
			}
			
			/*
			 * handling explicit cast of BitSizeType of width n to IntSizeType of width n. Note that the Integer literal of the form 16w100 is considered bit<16> type and 16s100 is considered int<16> type as per p4 spec. Thus handling all those cases appropriately
			e.g., following should be valid
			
			int<18> a; 
			bit<18> b; 
			a = (int<18>)b;
			a = (int<18>)18w88;
			a = (int<18>)(bit<18)(18s100);
			a = (int<18>)(18w100+18w800);
			and so on
			*
			*/
			if ("BitSizeType".equals(typeName2)){
				if (typeWidth1==typeWidth2){
					return true;
				}
			}
			

			/*Irrespective of the width of the LValue IntSizeType, it will be changed to the width of the right side IntSizeType which is handled in getType() above
			
			Handling explicit cast of IntSizeType of different bit width.
			e.g., following  should be valid
			
			
			int<18> a; 
			int<8> b; 
			a = (int<18>)b;
			a = (int<18>)8s10;//8 bit signed int to 18 bit
			
			
			int<8> c; 
			int<18> d; 
			c = (int<8>)d;
			c = (int<8>)16s8;//16 bit to 8 bit signed type
			
			In first case, there will be widening of the width. The compiler should  extend it with the sign bit.
			In second case, there will be loss of the data as the width is reduced.
			*
			*/
			if ("IntSizeType".equals(typeName2)){
				return true;
			}
		}
		
		if ("BitSizeType".equals(typeName1)){
			
			/*
			 * 
			 * Handling explicit cast of IntegerLiteral say 888 to bit<w>
			 */
			if ("IntegerLiteral".equals(typeName2)){
				return true;
			}
			
			/*
			 * handling explicit cast of IntSizeType of width n to BitSizeType of width n. Note that the Integer literal of the form 16w100 is considered bit<16> type and 16s100 is considered int<16> type as per p4 spec. Thus handling all those cases appropriately
			e.g., following should be valid
			
			bit<18> a; 
			int<18> b; 
			a = (bit<18>)b;
			a = (bit<18>)18s88;
			a = (bit<18>)(int<18)(18w100);
			a = (bit<18>)(18s100+18s800);
			and so on
			
			*
			*/
			if ("IntSizeType".equals(typeName2)){
				if (typeWidth1==typeWidth2){
					return true;
				}
			}
			
			/*
			 * handling explicit cast of bool to BitSizeType of width 1
			e.g., following should be valid
			
			bit<1> a; 
			bool b; 
			a = (bit<1>)b;
			a = (bit<1>)true;
			
			*
			*/
			if ("BoolType".equals(typeName2) || "BooleanLiteralTrue".equals(typeName2) || "BooleanLiteralFalse".equals(typeName2)){
				if (typeWidth1==1){
					return true;
				}
			}
			
			/*Irrespective of the width of the LValue BitSizeType, it will be changed to the width of the right side BitSizeType which is handled in getType() above
			
			Handling explicit cast of BitSizeType of different bit width.
			e.g., following both should be valid
			
			
			bit<18> a; 
			bit<8> b; 
			a = (bit<18>)b;
			a = (bit<18>)8w10;//8 bit unsigned bit type to 18 bit unsigned bit type

			
			
			bit<8> c; 
			bit<18> d; 
			c = (bit<8>)d;
			c = (bit<8>)16w8;//16 bit to 8 bit unsigned type

			
			In first case, there will be widening of the width. The compiler should pad zeros.
			In second case, there will be loss of the data as the width is reduced.
			*
			*/
			if ("BitSizeType".equals(typeName2)){
				return true;
			}
		}
		
		if ("BitType".equals(typeName1)){
					
			/*
			 * handling explicit cast of bool to BitType
			e.g., following should be valid
			
			bit a; 
			bool b; 
			a = (bit)b;
			a = (bit)false;
			
			*
			*/
			if ("BoolType".equals(typeName2) || "BooleanLiteralTrue".equals(typeName2) || "BooleanLiteralFalse".equals(typeName2)){
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String getTypeName(){
		return thisCastType;
	}
	
	@Override
	public int getTypeSize(){
		return thisCastSize;
	}
}
