package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.OrContext;



import java.util.LinkedHashMap;

public class OrContextExt extends ExpressionContextExt {

	private static final Logger L = LoggerFactory.getLogger(OrContextExt.class);
	
	private String thisTypeName;
	private int thisTypeSize;

	public OrContextExt(OrContext ctx) {
		super(ctx);
	}

	@Override
	public  OrContext getContext(){
		return (OrContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OrContext getContext(String str){
		return (OrContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OrContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OrContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isLogicalExpression(){return true;}
	@Override public boolean isBinaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(getContext().expression(0));}
	@Override public ExpressionNode getRight(){return (ExpressionNode) getExtendedContext(getContext().expression(1));}
	@Override public int getResultSize(){return 1;}
	@Override
	public String getOperator() {
		return getContext().LOR().getText().trim();
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		//do nothing, as operates on bool type
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((OrContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((OrContext)getContext()).expression(1));
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
			thisTypeName = "BoolType";//Looks semantically valid '||' operation. Thus giving it a type name and return this object itself as a type.
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
		
		if (("BooleanLiteralTrue".equals(typeName1) || "BooleanLiteralFalse".equals(typeName1) || "BoolType".equals(typeName1)) 
				&& ("BooleanLiteralTrue".equals(typeName2) || "BooleanLiteralFalse".equals(typeName2) || "BoolType".equals(typeName2) )){
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
