package com.p4.p416.util;

import java.math.BigInteger;

import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.IntegerContextExt;
import com.p4.p416.gen.NumberContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.NumberContext;

public class ExpressionEvalVisitor extends P416BaseVisitor<ParserRuleContext>{
	
	@Override
	public ParserRuleContext visitInteger(P416Parser.IntegerContext ctx)
	{
		NumberContext numberContext = ctx.number();
		NumberContextExt numberContextExt = (NumberContextExt) AbstractBaseExt.getExtendedContext(numberContext);
		String integerValue = "";
		if (numberContext.realNumber() != null) {
			String realNumber = numberContextExt.getFormattedText();
			integerValue = new BigInteger(realNumber.trim()).toString(10);
		}
		else if (numberContext.octalNumber() != null) {
			String realNumber = numberContextExt.getFormattedText();
			realNumber = realNumber.substring(2);
			integerValue = new BigInteger(realNumber.trim(), 8).toString(10);
		}
		else if (numberContext.binaryNumber() != null) {
			String realNumber = numberContextExt.getFormattedText();
			realNumber = realNumber.substring(2);
			integerValue = new BigInteger(realNumber.trim(), 2).toString(10);
		}
		else if (numberContext.decimalNumber() != null) {
			String realNumber = numberContextExt.getFormattedText();
			realNumber = realNumber.substring(2);
			integerValue = new BigInteger(realNumber.trim(), 2).toString(10);
		}
		ctx.extendedContext.value = integerValue;
		((IntegerContextExt)IntegerContextExt.getExtendedContext(ctx)).value = integerValue;
		return ctx;
		
	}
	
    //eval is partially implemented
}
