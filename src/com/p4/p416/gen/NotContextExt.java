package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.*;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.NotContext;
import com.p4.p416.gen.P416Parser.UnaryExpression_notContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

public class NotContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(NotContextExt.class);

	public NotContextExt(NotContext ctx) {
		super(ctx);
	}

	@Override
	public  NotContext getContext(){
		return (NotContext)contexts.get(contexts.size()-1);
	}

	@Override
	public NotContext getContext(String str){
		return (NotContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NotContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NotContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isLogicalExpression(){return true;}
	@Override public boolean isUnaryExpression(){return true;}
	@Override public ExpressionNode getLeft(){return (ExpressionNode) ((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression();}
	@Override public int getResultSize(){return 1;}
	@Override
	public String getOperator() {
		return getContext().unaryExpression_not().NOT().getText().trim();
	}
	
	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		NotContext nCtx = getContext();
		ExpressionContextExt expCtxE = (ExpressionContextExt) getExtendedContext(((UnaryExpression_notContext)getExtendedContext(nCtx.unaryExpression_not()).getContext()).expression());
		return solver.getCtx().mkNot((BoolExpr) super.getExpr(expCtxE.getLeft(), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes));
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		BigInteger value = super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
		BigInteger zero = new BigInteger("0");
		return value.equals(zero) ? new BigInteger("1") : new BigInteger("0");
	}
	
	@Override
	public Type getType(){
		((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeName();
		//if ("BoolType".equals(getContext().unaryExpression_not().expression().extendedContext.getTypeName())
		if ("BoolType".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeName())
		||	 ("BooleanLiteralTrue".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeName())
		||		 ("BooleanLiteralFalse".equals(((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeName()))))
			return getExtendedContext(((UnaryExpression_notContext)getExtendedContext(getContext().unaryExpression_not()).getContext()).expression()).getType();
		L.error("Line:"+getContext().start.getLine()+":  Not can't be done on non boolean :"+getExtendedContext(getContext()).getFormattedText());
		return null;
	}
	
	@Override
	public int getTypeSize(){
		return ((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeSize();
	}
	
	@Override
	public String getTypeName(){
		return ((ExpressionContextExt)getExtendedContext(((UnaryExpression_notContext) getExtendedContext(getContext().unaryExpression_not()).getContext()).expression())).getTypeName();
	}
}
