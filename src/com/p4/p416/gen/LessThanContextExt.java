package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.LessThanContext;

public class LessThanContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(LessThanContextExt.class);

	private String thisTypeName;

	private int thisTypeSize;

	public LessThanContextExt(LessThanContext ctx) {
		super(ctx);
	}

	@Override
	public  LessThanContext getContext(){
		return (LessThanContext)contexts.get(contexts.size()-1);
	}

	@Override
	public LessThanContext getContext(String str){
		return (LessThanContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof LessThanContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ LessThanContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext((LessThanContext)getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext((LessThanContext)getContext().expression(1));}
	@Override public int getResultSize(){return 1;}
	@Override
	public String getOperator() {
		return getContext().LT().getText().trim();
	}

	public boolean isBooleanExpression() {
		return true;
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((LessThanContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((LessThanContext)getContext()).expression(1));
		Type leftType = leftExpression.getType();
		Type rightType = rightExpression.getType();
		
		if (leftType==null || rightType==null){
			return null;
		}
		
		if (!(this.isTypeCompatible(leftType, rightType))){
            L.error("Line:"+getContext().start.getLine()+": '"+leftExpression.getFormattedText()+ "' is incompatible with '"+rightExpression.getFormattedText()+"'");
            return null;
		}
		else{
			thisTypeName = "BoolType";//Looks semantically valid "<" operation. Thus giving it a type name and return this object itself as a type.
			thisTypeSize = 1;
		}
		return this;
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
		
		//Handling Integer literals of the form: 8 < 9 or (8 < (9/7)) or (8 < expr) or (expr < 8) or (expr < expr) where expr is any expression whose type is IntegerLiteral
		if ("IntegerLiteral".equals(typeName1) && "IntegerLiteral".equals(typeName2)){
			return true;
		}
		
		//If both typeName and typeSize is equal
		if ((typeName1.equals(typeName2)) && (type1Size == type2Size)){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getTypeName(){
		return thisTypeName;
	}
	
	@Override
	public int getTypeSize(){
		return thisTypeSize;
	}
}
