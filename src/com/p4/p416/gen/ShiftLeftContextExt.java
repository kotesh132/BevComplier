package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ShiftLeftContext;

public class ShiftLeftContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(ShiftLeftContextExt.class);

	public ShiftLeftContextExt(ShiftLeftContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ShiftLeftContext getContext(){
		return (ShiftLeftContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShiftLeftContext getContext(String str){
		return (ShiftLeftContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ShiftLeftContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ShiftLeftContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext(getContext().expression(1));}

	@Override
	public String getOperator() {
		return getContext().SHL().getText().trim();
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((ShiftLeftContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((ShiftLeftContext)getContext()).expression(1));
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
		
		String typeName1 = type1.getTypeName();
		String typeName2 = type2.getTypeName();
		int type1Size = type1.getTypeSize();
		int type2Size = type2.getTypeSize();
		
		//TODO: find out whether the type of RHS should be implicitly typecast. Currently not doing it as I did not find clear documentation on it.
		//Currently just checking whether it is a valid cast
		if (((("BitSizeType".equals(typeName1) || "IntSizeType".equals(typeName1)) && (type1Size>0))) || ("IntegerLiteral".equals(typeName1)) && 
				("BitSizeType".equals(typeName2) && (type2Size>0)) ){//Compiler must handle - IntegerLiteral (Expression or literal) value should not be negative
			//TODO: add overflow error
			return true;
		}
		if (((("BitSizeType".equals(typeName1) || "IntSizeType".equals(typeName1)) && (type1Size>0))) || ("IntegerLiteral".equals(typeName1)) && 
				("IntegerLiteral".equals(typeName2))){//Compiler must handle that the IntegerLiteral (Expression or literal) value should not be negative
			//TODO: add overflow error
			return true;
		}
		return false;
	}
}
