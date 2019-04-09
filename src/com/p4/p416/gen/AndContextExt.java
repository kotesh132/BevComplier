package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.*;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.AndContext;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

public class AndContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(AndContextExt.class);
	
	private String thisTypeName;
	private int thisTypeSize;

	public AndContextExt(AndContext ctx) {
		super(ctx);
	}

	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public AndContext getContext(String str){
		return (AndContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof AndContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ AndContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override public boolean isLogicalExpression(){return true;}
	@Override public boolean isBinaryExpression(){return true;}

	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof AndContext) {
			return (ExpressionContextExt)getExtendedContext(((AndContext) getContext()).expression(0));
		}
		return null;
	}

	@Override
	public ExpressionNode getRight() {
		if (getContext() instanceof AndContext) {
			return (ExpressionContextExt)getExtendedContext(((AndContext) getContext()).expression(1));
		}
		return null;
	}
	@Override public int getResultSize(){return 1;}
	@Override
	public String getOperator() {
		if (getContext() instanceof AndContext) {
			return ((AndContext)getContext()).LAND().getText().trim();
		}
		return null;
	}

	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		if (getContext() instanceof AndContext) {
			AndContext andCtx = (AndContext) getContext();
			List<BoolExpr> list = new ArrayList<BoolExpr>();
			for (ExpressionContext expCtx : andCtx.expression()) {
				ExpressionContextExt expCtxE = (ExpressionContextExt) getExtendedContext(expCtx);
				list.add((BoolExpr) expCtxE.gatherSymbolsAndConstraints(solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes));
			}
			return solver.getCtx().mkAnd(list.toArray(new BoolExpr[list.size()]));
		}
		return null;
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		//do nothing, as operates on bool type
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		BigInteger left = super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
		BigInteger right = super.eval(getRight(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
		BigInteger zero = new BigInteger("0");
		return left.compareTo(zero) == 1 && right.compareTo(zero) == 1 ? new BigInteger("1") : new BigInteger("0");
	}
	
	@Override
	public Type getType(){
		
		ExpressionContextExt leftExpression = ((ExpressionContextExt)getExtendedContext(((AndContext)getContext()).expression(0)));
		ExpressionContextExt rightExpression = (ExpressionContextExt)getExtendedContext(((AndContext)getContext()).expression(1));
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
			thisTypeName = "BoolType";//Looks semantically valid '&&' operation. Thus giving it a type name and return this object itself as a type.
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
