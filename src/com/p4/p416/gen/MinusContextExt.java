package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.MinusContext;

public class MinusContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(MinusContextExt.class);

	public MinusContextExt(MinusContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  MinusContext getContext(){
		return (MinusContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MinusContext getContext(String str){
		return (MinusContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MinusContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MinusContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext((MinusContext)getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext((MinusContext)getContext().expression(1));}
	@Override
	public String getOperator() {
		return getContext().MINUS().getText().trim();
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((MinusContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((MinusContext)getContext()).expression(1));
		Type leftType = leftExpression.getType();
		Type rightType = rightExpression.getType();
		
		if (leftType==null || rightType==null){
			return null;
		}
		
		if (!(this.isTypeCompatible(leftType, rightType))){
            L.error("Line:"+getContext().start.getLine()+": '"+leftExpression.getFormattedText()+ "' is incompatible with '"+rightExpression.getFormattedText()+"'");
            return null;
		}
		return leftType;
	}
	
	@Override
	public boolean isTypeCompatible(Type type1, Type type2){
		if (type1==null || type2==null){
			return false;
		}
		if (type1.equals(type2)){
			return true;
		}
		String typeName1 = type1.getTypeName();
		String typeName2 = type2.getTypeName();
		int type1Size = type1.getTypeSize();
		int type2Size = type2.getTypeSize();
		
		//Handling Integer literals of the form: 8-9 or (8-(9/7)) or (8-expr) or (expr-8) or (expr-expr) where expr is any expression whose type is IntegerLiteral
		if ("IntegerLiteral".equals(typeName1) && "IntegerLiteral".equals(typeName2) && type1Size==type2Size){
			return true;
		}
		
		//Implicit Typecast check: Handling Variable and Integer literals of the form: '8-a' where a can itself be any expression or any typed variable of type bit<8>, bit<18>, int<16>, etc
		if ("IntegerLiteral".equals(typeName1)){
			((IntegerContextExt)type1).setTypeName(type2.getTypeName());
			((IntegerContextExt)type1).setTypeSize(type2.getTypeSize());
			return true;
		}
		
		//Implicit Typecast check: Handling Variable and Integer literals of the form: 'a-8' where a can itself be any expression or any typed variable of type bit<8>, bit<18>, int<16>, etc
		if ("IntegerLiteral".equals(typeName2)){
			((IntegerContextExt)type2).setTypeName(type1.getTypeName());
			((IntegerContextExt)type2).setTypeSize(type1.getTypeSize());
			return true;
		}
		
		//Can't do minus operation on bools
		if ("BooleanLiteralTrue".equals(typeName1) || "BooleanLiteralFalse".equals(typeName1) || "BoolType".equals(typeName1) 
				|| "BooleanLiteralTrue".equals(typeName2) || "BooleanLiteralFalse".equals(typeName2) || "BoolType".equals(typeName2) ){
			L.error("Line:"+getContext().start.getLine()+":  Subtraction can't be done on boolean :"+getExtendedContext(getContext()).getFormattedText());
			return false;
		}
		
		//If both typeName and typeSize is equal
		if ((typeName1.equals(typeName2)) && (type1Size == type2Size)){
			return true;
		}
		
		if (type1.equals(type2)){
			return true;
		}
		
		return false;
	}
}
