package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.PlusContext;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.BitXorContext;
import com.p4.utils.Utils.Pair;

public class BitXorContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(BitXorContextExt.class);

	public BitXorContextExt(BitXorContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitXorContext getContext(String str){
		return (BitXorContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BitXorContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ BitXorContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}
	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof BitXorContext) {
			return (ExpressionContextExt)getExtendedContext(((BitXorContext)getContext()).expression(0));
		}
		return null;
	}

	@Override
	public ExpressionNode getRight() {
		if (getContext() instanceof BitXorContext) {
			return  (ExpressionContextExt)getExtendedContext(((BitXorContext) getContext()).expression(1));
		}
		return null;
	}

	@Override
	public String getOperator() {
		if (getContext() instanceof BitXorContext) {
			return ((BitXorContext)getContext()).XOR().getText().trim();
		}
		return null;
	}
	
	
	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		ExpressionContextExt left = (ExpressionContextExt) getLeft();
		ExpressionContextExt right = (ExpressionContextExt) getRight();
		return left.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes) ^ right.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		return super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData).xor(super.eval(getRight(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData));
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = ((BitXorContext)(this.getContext())).expression(0).extendedContext;
		ExpressionContextExt rightExpression = ((BitXorContext)(this.getContext())).expression(1).extendedContext;
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
		
		//Handling Integer literals of the form: 8^9 or (8^(9/7)) or (8^expr) or (expr^8) or (expr^expr) where expr is any expression whose type is IntegerLiteral
		if ("IntegerLiteral".equals(typeName1) && "IntegerLiteral".equals(typeName2) && type1Size==type2Size){
			return true;
		}
		
		
		//Can't do add operation on bools
		if ("BooleanLiteralTrue".equals(typeName1) || "BooleanLiteralFalse".equals(typeName1) || "BoolType".equals(typeName1) 
				|| "BooleanLiteralTrue".equals(typeName2) || "BooleanLiteralFalse".equals(typeName2) || "BoolType".equals(typeName2) ){
			L.error("Line:"+getContext().start.getLine()+":  Addition can't be done on boolean :"+getExtendedContext(getContext()).getFormattedText());
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


