package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Expr;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.PlusContext;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

public class PlusContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(PlusContextExt.class);

	public PlusContextExt(PlusContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlusContext getContext(String str){
		return (PlusContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PlusContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PlusContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}

	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof PlusContext) {
			return (ExpressionNode) getExtendedContext(((PlusContext) getContext()).expression(0));
		}
		return null;
	}

	@Override
	public ExpressionNode getRight() {
		if (getContext() instanceof PlusContext) {
			return (ExpressionNode) getExtendedContext(((PlusContext) getContext()).expression(1));
		}
		return null;
	}

	@Override
	public String getOperator() {
		if (getContext() instanceof PlusContext) {
			return ((PlusContext) getContext()).PLUS().getText().trim();
		}
		return null;
	}
	
	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		ExpressionContextExt left = (ExpressionContextExt) getLeft();
		ExpressionContextExt right = (ExpressionContextExt) getRight();
		return left.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes) + right.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		return super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData).add(super.eval(getRight(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData));
	}
	
	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		PlusContextExt pCtxE = (PlusContextExt) getExtendedContext(getContext());
		return solver.getCtx().mkAdd((ArithExpr)super.getExpr(pCtxE.getLeft(), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes), (ArithExpr)super.getExpr(pCtxE.getRight(), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes));
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((PlusContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((PlusContext)getContext()).expression(1));
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
		
		//Handling Integer literals of the form: 8+9 or (8+(9/7)) or (8+expr) or (expr+8) or (expr+expr) where expr is any expression whose type is IntegerLiteral
		if ("IntegerLiteral".equals(typeName1) && "IntegerLiteral".equals(typeName2) && type1Size==type2Size){
			return true;
		}
		
		//Implicit Typecast check: Handling Variable and Integer literals of the form: '8+a' where a can itself be any expression or any typed variable of type bit<8>, bit<18>, int<16>, etc
		if ("IntegerLiteral".equals(typeName1)){
			((IntegerContextExt)type1).setTypeName(type2.getTypeName());
			((IntegerContextExt)type1).setTypeSize(type2.getTypeSize());
			return true;
		}
		
		//Implicit Typecast check: Handling Variable and Integer literals of the form: 'a+8' where a can itself be any expression or any typed variable of type bit<8>, bit<18>, int<16>, etc
		if ("IntegerLiteral".equals(typeName2)){
			((IntegerContextExt)type2).setTypeName(type1.getTypeName());
			((IntegerContextExt)type2).setTypeSize(type1.getTypeSize());
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
