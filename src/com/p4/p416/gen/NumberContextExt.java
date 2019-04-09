package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.p4.p416.iface.INumber;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.NumberContext;

public class NumberContextExt extends AbstractBaseExt implements INumber{

	public NumberContextExt(NumberContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  NumberContext getContext(){
		return (NumberContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NumberContext getContext(String str){
		return (NumberContext)new PopulateExtendedContextVisitor().visit(getParser(str).number());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NumberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NumberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void preProcess(){
		NumberContext ctx = (NumberContext) getContext();
		Pattern p = null;
		Matcher m = null;
		if(ctx.decimalNumber() != null){
			 p = Pattern.compile("([0-9]+[ws])?'0'[dD][0-9_]+");
			 m = p.matcher(ctx.getText());
		} else if(ctx.hexNumber() != null){
			 p = Pattern.compile("([0-9]+[ws])?0[xX][0-9a-fA-F_]+");
			 m = p.matcher(ctx.getText());
		} else if(ctx.octalNumber() != null){
			 p = Pattern.compile("([0-9]+[ws])?0[oO][0-7_]+");
			 m = p.matcher(ctx.getText());
		} else if(ctx.binaryNumber() != null){
			 p = Pattern.compile("([0-9]+[ws])?0[bB][01_]+");
			 m = p.matcher(ctx.getText());
		} else if(ctx.realNumber() != null){
			 p = Pattern.compile("([0-9]+[ws])?[0-9_]+");
			 m = p.matcher(ctx.getText());
		}
		if(m.find()){
			if(m.group(1) != null){
				addToContexts(getContext(ctx.getText().replace(m.group(1), "")));
			}
		}
	}
	
	@Override
	protected String getSize(){
		return getContext().getText();
	}
	
	@Override
	public MemoryType getMemoryType(){
		if(this.getSizeInBits() >1 )
		{
			return MemoryType.TypeCfgByte;
		}
		else
		{
			return MemoryType.TypeCfgBit;
		}
    }

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		NumberContext context = getContext();
		String hexValue = "";
		if (context.realNumber() != null) {
			String realNumber = getFormattedText();
			hexValue = new BigInteger(realNumber.trim()).toString(16);
		}
		else if (context.octalNumber() != null) {
			String realNumber = getFormattedText();
			realNumber = realNumber.substring(2);
			hexValue = new BigInteger(realNumber.trim(), 8).toString(16);
		}
		else if (context.binaryNumber() != null) {
			String realNumber = getFormattedText();
			realNumber = realNumber.substring(2);
			hexValue = new BigInteger(realNumber.trim(), 2).toString(16);
		}
		else if (context.decimalNumber() != null) {
			String realNumber = getFormattedText();
			realNumber = realNumber.substring(2);
			hexValue = new BigInteger(realNumber.trim(), 2).toString(16);
		}

		if (!hexValue.isEmpty()) {
			setContext(getContext("0x" + hexValue));
		}
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public int getTypeSize(){
		String numberString = this.getFormattedText();
		if (numberString.contains("w")){
			return Integer.parseInt(numberString.substring(0, numberString.indexOf("w")));
		}
		else if (numberString.contains("s")){
			return Integer.parseInt(numberString.substring(0, numberString.indexOf("s")));
		}
		else {
			return this.getSizeInBits();
		}
	}
	
	@Override
	public String getTypeName(){
		String numberString = this.getFormattedText();
		if (numberString.contains("w")){
			return "BitSizeType";
		}
		else if (numberString.contains("s")){
			return "IntSizeType";
		}
		else {
			return "IntegerLiteral";
		}
	}
}
