package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.microsoft.z3.Expr;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.OfContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfContextExt extends ExpressionContextExt {

	private static final Logger L = LoggerFactory.getLogger(OfContextExt.class);

	public OfContextExt(OfContext ctx) {
		super(ctx);
	}

	@Override
	public  OfContext getContext(){
		return (OfContext)contexts.get(contexts.size()-1);
	}

	@Override
	public OfContext getContext(String str){
		return (OfContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OfContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OfContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isUnaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) getExtendedContext(getContext().expression());}
	@Override public boolean isTerminal(){return getLeft().isTerminal();}
	@Override public String TerminalValue(){
		if(isTerminal()){
			return getLeft().TerminalValue();
		}else{
			throw new RuntimeException();
		}
	}
	
	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		return super.getExpr((ExpressionNode) getExtendedContext(getContext().expression()),solver,bs,headerValidLocations,headerFieldsOffsetsAndSizes);
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		return super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		L.warn("OfContext yet to be handled properly : " + getFormattedText());
	}
	
	@Override
	public Type getType(){
		return getExtendedContext(getContext().expression()).getType();
	}
	
	@Override
	public String getTypeName(){
		return this.getType().getTypeName();
	}
}
