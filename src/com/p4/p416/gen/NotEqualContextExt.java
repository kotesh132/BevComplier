package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Map;

import com.p4.p416.gen.P416Parser.ExpressionContext;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.Expr;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.NotEqualContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

public class NotEqualContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(NotEqualContextExt.class);
	
	private String thisTypeName;
	private int thisTypeSize;

	public NotEqualContextExt(NotEqualContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionContext getContext(){
		return (ExpressionContext) contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NotEqualContext getContext(String str){
		return (NotEqualContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NotEqualContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NotEqualContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public boolean isBinaryExpression(){return true;}

	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof NotEqualContext) {
			return (ExpressionNode) getExtendedContext(((NotEqualContext) getContext()).expression(0));
		}
		return null;
	}

	@Override
	public ExpressionNode getRight() {
		if (getContext() instanceof NotEqualContext) {
			return (ExpressionNode) getExtendedContext(((NotEqualContext) getContext()).expression(1));
		}
		return null;
	}

	@Override
	public int getResultSize() {
		return 1;
	}

	@Override
	public String getOperator() {
		if (getContext() instanceof NotEqualContext) {
			return ((NotEqualContext) getContext()).NE().getText().trim();
		}
		return null;
	}
	
	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		NotEqualContextExt neCtxE = (NotEqualContextExt) getExtendedContext(getContext());
		return solver.getCtx().mkNot(solver.getCtx().mkEq(super.getExpr(neCtxE.getLeft(), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes), super.getExpr(neCtxE.getRight(), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes)));
	}

	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		BigInteger left = super.eval(getLeft(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
		BigInteger right = super.eval(getRight(), byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
 		return left.equals(right) ? new BigInteger("0") : new BigInteger("1");
	}

	public boolean isBooleanExpression() {
		return true;
	}
	
	@Override
	public Type getType(){
		ExpressionContextExt leftExpression = (ExpressionContextExt) getExtendedContext(((NotEqualContext)getContext()).expression(0));
		ExpressionContextExt rightExpression = (ExpressionContextExt) getExtendedContext(((NotEqualContext)getContext()).expression(1));
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
			thisTypeName = "BoolType";//Looks semantically valid !=. Thus giving it a type name and return this object itself as a type.
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
		
		//Handling Integer literals of the form: 8 != 9 or (8 != (9/7)) or (8 != expr) or (expr != 8) or (expr != expr) where expr is any expression whose type is IntegerLiteral
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
