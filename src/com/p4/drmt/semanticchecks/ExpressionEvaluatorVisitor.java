package com.p4.drmt.semanticchecks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.FunctionCallContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.ActionDeclarationContext;
import com.p4.p416.gen.P416Parser.ArgumentContext;
import com.p4.p416.gen.P416Parser.InstantiationContext;
import com.p4.p416.gen.P416Parser.ParameterContext;

public class ExpressionEvaluatorVisitor extends P416BaseVisitor<ParserRuleContext> {
	
	private static final Logger L = LoggerFactory.getLogger(ExpressionEvaluatorVisitor.class);
	
	boolean insideActionBlock = false;

	@Override
	public ParserRuleContext visitInstantiation(InstantiationContext ctx) {
		super.visitInstantiation(ctx);
		String typeName = AbstractBaseExt.getExtendedContext(ctx.typeRef()).getTypeName();
		Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeName);
		if ("control".equals(symbol.getTypeName())){
			if (ctx.argumentList()!=null){
				for (ArgumentContext arg:ctx.argumentList().argument()){
					if (arg.expression()!=null && AbstractBaseExt.getExtendedContext(arg).getNewValue()==null)
						L.error("Line:"+ctx.start.getLine()+": Expression '"+AbstractBaseExt.getExtendedContext(arg).getFormattedText()+"' can't be evaluated to a compile time constant.");
				}
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitArgument(ArgumentContext ctx) {
		super.visitArgument(ctx);
		if (ctx.expression()!=null){
			Integer newValue = AbstractBaseExt.getExtendedContext(ctx.expression()).getNewValue();
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(newValue);
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx) {
		super.visitAssignmentStatement(ctx);
		try{
			Symbol lValueSymbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx.lvalue()).getFormattedText());
			Integer rValueIntValue = AbstractBaseExt.getExtendedContext(ctx.expression()).getNewValue();
			String rValueType = AbstractBaseExt.getExtendedContext(ctx.expression()).getType().getTypeName();
			if (isBaseType(rValueType) && !(AbstractBaseExt.getExtendedContext(ctx.expression()).getType() instanceof FunctionCallContextExt)){
			if (rValueIntValue!=null){
				lValueSymbol.setNewValue(rValueIntValue);	
			}
			else{	
				L.error("Line:"+ctx.start.getLine()+": Expression "+AbstractBaseExt.getExtendedContext(ctx.expression()).getFormattedText()+" can't be evaluated at compile time. "+ctx.lvalue().getText()+" is not a compile time constant");
			}
			}
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitPlus(P416Parser.PlusContext ctx) {
		super.visitPlus(ctx);
		try{
			Integer expr1Value = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
			Integer expr2Value = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();			
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(expr1Value+expr2Value);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	
	@Override
	public ParserRuleContext visitMinus(P416Parser.MinusContext ctx) {
		super.visitMinus(ctx);
		try{
			Integer expr1Value = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
			Integer expr2Value = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();			
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(expr1Value-expr2Value);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+ AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitStar(P416Parser.StarContext ctx) {
		super.visitStar(ctx);
		try{
			Integer expr1Value = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
			Integer expr2Value = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();			
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(expr1Value*expr2Value);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitSlah(P416Parser.SlahContext ctx) {
		super.visitSlah(ctx);
		try{
			Integer expr1Value = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
			Integer expr2Value = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();	
			if ((expr1Value !=null && expr1Value<0) || (expr2Value !=null && expr2Value<0)){
				L.error("Line:"+ctx.start.getLine()+": Division not defined on negative values: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
			
			//Now checking the signed-ness
			Type type1 = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getType();
			Type type2 = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getType();
			if ( (type1!=null && "IntSizeType".equals(type1.getTypeName())) || (type2!=null && "IntSizeType".equals(type2.getTypeName()))){
				L.error("Line:"+ctx.start.getLine()+": Division not defined on signed types: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
			if (expr2Value!=null && expr2Value!=0)
				AbstractBaseExt.getExtendedContext(ctx).setNewValue(expr1Value/expr2Value);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitMod(P416Parser.ModContext ctx) {
		super.visitMod(ctx);
		try{
			Integer expr1Value = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
			Integer expr2Value = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();	
			if ((expr1Value !=null && expr1Value<0) || (expr2Value !=null && expr2Value<0)){
				L.error("Line:"+ctx.start.getLine()+": Modulo not defined on negative values: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
			
			//Now checking the signed-ness
			Type type1 = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getType();
			Type type2 = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getType();
			if ( (type1!=null && "IntSizeType".equals(type1.getTypeName())) || (type2!=null && "IntSizeType".equals(type2.getTypeName()))){
				L.error("Line:"+ctx.start.getLine()+": Modulo not defined on signed types: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}

			
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(expr1Value%expr2Value);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitNonType(P416Parser.NonTypeContext ctx) {
		super.visitNonType(ctx);
		try{
		AbstractBaseExt.getExtendedContext(ctx).setNewValue(AbstractBaseExt.getExtendedContext(ctx.nonTypeName()).getNewValue());
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitNonTypeName(P416Parser.NonTypeNameContext ctx) {
		super.visitNonTypeName(ctx);
		try{
		Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
//		L.info(":::"+symbol.getTypeName()+":::::"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
		if (isBaseType(symbol.getTypeName())){
		Integer newValue =  symbol.getNewValue();
		AbstractBaseExt.getExtendedContext(ctx).setNewValue(newValue);
		}
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitName(P416Parser.NameContext ctx){
		super.visitName(ctx);
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitVariableDeclaration(P416Parser.VariableDeclarationContext ctx){
		super.visitVariableDeclaration(ctx);
		try{
		if (ctx.optInitializer()!=null){
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(AbstractBaseExt.getExtendedContext(ctx.optInitializer().initializer().expression()).getNewValue());
		}
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitInteger(P416Parser.IntegerContext ctx) {
		super.visitInteger(ctx);
		try{
		//TODO: Parse not pure integers too here
		
		//Trying to honor non pure integers like 32s1, 32b1 etc here --start
			String actualNumberString = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
			Pattern p = null;
			Matcher m = null;
			if(ctx.number().decimalNumber() != null){
				 p = Pattern.compile("([0-9]+[ws])?'0'[dD][0-9_]+");
				 m = p.matcher(ctx.getText());
			} else if(ctx.number().hexNumber() != null){
				 p = Pattern.compile("([0-9]+[ws])?0[xX][0-9a-fA-F_]+");
				 m = p.matcher(ctx.getText());
			} else if(ctx.number().octalNumber() != null){
				 p = Pattern.compile("([0-9]+[ws])?0[oO][0-7_]+");
				 m = p.matcher(ctx.getText());
			} else if(ctx.number().binaryNumber() != null){
				 p = Pattern.compile("([0-9]+[ws])?0[bB][01_]+");
				 m = p.matcher(ctx.getText());
			} else if(ctx.number().realNumber() != null){
				 p = Pattern.compile("([0-9]+[ws])?[0-9_]+");
				 m = p.matcher(ctx.getText());
			}
			
		if(m.find()){
			if(m.group(1) != null){
//				L.info("****m.group(1): "+m.group(1));
				actualNumberString = ctx.getText().replace(m.group(1), "");
			}
		}
			
		//Trying to honor non pure integers like 32s1, 32b1 etc here --end
			
		Integer newValue = Integer.parseInt(actualNumberString);
		AbstractBaseExt.getExtendedContext(ctx).setNewValue(newValue);
		}
		catch(Throwable e){
			L.debug("Line:"+ctx.start.getLine()+": Error Evaluating: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+". Exception Message: "+e.getMessage());
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx) {
		// TODO Auto-generated method stub
		return super.visitPrefixedNonTypeName(ctx);
	}

	@Override
	public ParserRuleContext visitRangeIndex(P416Parser.RangeIndexContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRangeIndex(ctx);
	}

	@Override
	public ParserRuleContext visitString(P416Parser.StringContext ctx) {
		// TODO Auto-generated method stub
		return super.visitString(ctx);
	}

	@Override
	public ParserRuleContext visitBitOr(P416Parser.BitOrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitBitOr(ctx);
	}

	@Override
	public ParserRuleContext visitCast(P416Parser.CastContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCast(ctx);
	}

	@Override
	public ParserRuleContext visitNot(P416Parser.NotContext ctx) {
		// TODO Auto-generated method stub
		return super.visitNot(ctx);
	}

	@Override
	public ParserRuleContext visitShiftLeft(P416Parser.ShiftLeftContext ctx) {
		// TODO Auto-generated method stub
		return super.visitShiftLeft(ctx);
	}

	@Override
	public ParserRuleContext visitPlusPlus(P416Parser.PlusPlusContext ctx) {
		// TODO Auto-generated method stub
		return super.visitPlusPlus(ctx);
	}

	@Override
	public ParserRuleContext visitAnd(P416Parser.AndContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAnd(ctx);
	}

	@Override
	public ParserRuleContext visitOf(P416Parser.OfContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOf(ctx);
	}

	@Override
	public ParserRuleContext visitLessThan(P416Parser.LessThanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLessThan(ctx);
	}

	@Override
	public ParserRuleContext visitTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTempletizedFunctionCall(ctx);
	}

	@Override
	public ParserRuleContext visitGreaterThan(P416Parser.GreaterThanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitGreaterThan(ctx);
	}

	@Override
	public ParserRuleContext visitMemberAccess(P416Parser.MemberAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitMemberAccess(ctx);
	}

	@Override
	public ParserRuleContext visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExprMemberAccess(ctx);
	}

	@Override
	public ParserRuleContext visitSet(P416Parser.SetContext ctx) {
		// TODO Auto-generated method stub
		return super.visitSet(ctx);
	}

	@Override
	public ParserRuleContext visitShifRight(P416Parser.ShifRightContext ctx) {
		// TODO Auto-generated method stub
		return super.visitShifRight(ctx);
	}

	@Override
	public ParserRuleContext visitOr(P416Parser.OrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOr(ctx);
	}

	@Override
	public ParserRuleContext visitFalse(P416Parser.FalseContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFalse(ctx);
	}

	@Override
	public ParserRuleContext visitConstructor(P416Parser.ConstructorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitConstructor(ctx);
	}

	@Override
	public ParserRuleContext visitNotEqual(P416Parser.NotEqualContext ctx) {
		// TODO Auto-generated method stub
		return super.visitNotEqual(ctx);
	}


	@Override
	public ParserRuleContext visitGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx) {
		// TODO Auto-generated method stub
		return super.visitGreaterThanOrEqual(ctx);
	}

	@Override
	public ParserRuleContext visitEqual(P416Parser.EqualContext ctx) {
		// TODO Auto-generated method stub
		return super.visitEqual(ctx);
	}

	@Override
	public ParserRuleContext visitParameter(ParameterContext ctx) {
		super.visitParameter(ctx);
		//We should mark this is a valid with respect to value though value is not got at compile time for action blocks. If there are other params (in other kind of invocations too), we need to take care of them as well
		if (insideActionBlock){
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(0);
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx.name()).getFormattedText());
			symbol.setNewValue(0);
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitActionDeclaration(ActionDeclarationContext ctx) {
		insideActionBlock = true;
		super.visitActionDeclaration(ctx);
		insideActionBlock = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitBitAnd(P416Parser.BitAndContext ctx) {
		super.visitBitAnd(ctx);
		Integer value1 = AbstractBaseExt.getExtendedContext(ctx.expression(0)).getNewValue();
		Integer value2 = AbstractBaseExt.getExtendedContext(ctx.expression(1)).getNewValue();
		if (value1==null || value2==null){
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(null);
		}else{
			AbstractBaseExt.getExtendedContext(ctx).setNewValue(value1&value2);
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitUnaryPlus(P416Parser.UnaryPlusContext ctx) {
		// TODO Auto-generated method stub
		return super.visitUnaryPlus(ctx);
	}

	@Override
	public ParserRuleContext visitNegate(P416Parser.NegateContext ctx) {
		// TODO Auto-generated method stub
		return super.visitNegate(ctx);
	}

	@Override
	public ParserRuleContext visitBitXor(P416Parser.BitXorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitBitXor(ctx);
	}

	@Override
	public ParserRuleContext visitLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLessThanOrEqual(ctx);
	}

	@Override
	public ParserRuleContext visitFunctionCall(P416Parser.FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionCall(ctx);
	}

	@Override
	public ParserRuleContext visitTrue(P416Parser.TrueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTrue(ctx);
	}

	@Override
	public ParserRuleContext visitUnaryMinus(P416Parser.UnaryMinusContext ctx) {
	    super.visitUnaryMinus(ctx);
	    if (AbstractBaseExt.getExtendedContext(ctx.unaryExpression_minus().expression()).getNewValue()!=null)
	    	AbstractBaseExt.getExtendedContext(ctx).setNewValue(-1*AbstractBaseExt.getExtendedContext(ctx.unaryExpression_minus().expression()).getNewValue());
	    return ctx;
	}

	@Override
	public ParserRuleContext visitErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitErrorMemberAccess(ctx);
	}

	@Override
	public ParserRuleContext visitArrayIndex(P416Parser.ArrayIndexContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayIndex(ctx);
	}

	@Override
	public ParserRuleContext visitTernary(P416Parser.TernaryContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTernary(ctx);
	}
	
	private boolean isBaseType(String typeName) {
		if ("BitSizeType".equals(typeName) 
				|| "IntSizeType".equals(typeName) 
					|| "IntegerLiteral".equals(typeName)
						|| "BoolType".equals(typeName)
							|| "BooleanLiteralTrue".equals(typeName)
								|| "BooleanLiteralFalse".equals(typeName)
									|| "BitType".equals(typeName)
										|| "VarBitSizeType".equals(typeName)
											|| "enum".equals(typeName))
			return true;
		return false;
	}
}