package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import com.p4.p416.gen.P416Parser.ExpressionContext;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.BitOrContext;
import com.p4.utils.Utils.Pair;

public class BitOrContextExt extends ExpressionContextExt {

	public BitOrContextExt(BitOrContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitOrContext getContext(String str){
		return (BitOrContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BitOrContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ BitOrContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}

	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof BitOrContext) {
			return (ExpressionContextExt)getExtendedContext(((BitOrContext)getContext()).expression(0));
		}
		return null;
	}

	@Override
	public ExpressionNode getRight() {
		if (getContext() instanceof BitOrContext) {
			return (ExpressionContextExt)getExtendedContext(((BitOrContext) getContext()).expression(1));
		}
		return null;
	}

	@Override
	public String getOperator() {
		if (getContext() instanceof BitOrContext) {
			return ((BitOrContext)getContext()).OR().getText().trim();
		}
		return null;
	}
	
	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		ExpressionContextExt left = (ExpressionContextExt) getLeft();
		ExpressionContextExt right = (ExpressionContextExt) getRight();
		return left.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes) | right.eval(byteVector, bitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		return super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData).or(super.eval(getRight(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData));
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(((BitOrContext)getContext()).expression(0)).getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(((BitOrContext)getContext()).expression(1)).getTypeSize();
	}
}
