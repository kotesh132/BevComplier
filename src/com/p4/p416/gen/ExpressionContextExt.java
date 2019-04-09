package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.BitAndContext;
import com.p4.p416.gen.P416Parser.BitOrContext;
import com.p4.p416.gen.P416Parser.BitXorContext;
import com.p4.p416.gen.P416Parser.EqualContext;
import com.p4.p416.gen.P416Parser.ExprMemberAccessContext;
import com.p4.p416.gen.P416Parser.GreaterThanContext;
import com.p4.p416.gen.P416Parser.IntegerContext;
import com.p4.p416.gen.P416Parser.LessThanContext;
import com.p4.p416.gen.P416Parser.NotEqualContext;
import com.p4.p416.gen.P416Parser.PlusContext;
import com.p4.p416.iface.IExpression;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.*;
import com.p4.drmt.parser.ByteUtils;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.packetgen.Z3Solver;
import com.p4.tools.graph.structs.TupleStatementSimplify;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public abstract class ExpressionContextExt extends AbstractBaseExt implements ExpressionNode, Summarizable, IExpression {

	public ExpressionContextExt(ParserRuleContext context) {
		super(context);
	}

	private static final Logger L = LoggerFactory.getLogger(ExpressionContextExt.class);

	protected List<ExpressionNode> simpleExpressions = new ArrayList<>();

	public String value;

	public static String getApplyPrefix(String input){
		int index = input.indexOf(".apply");
		return input.substring(0,index);
	}

	public boolean isApplyNode(){
		return this.getFormattedText().contains(".apply()");
	}

	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		throw new RuntimeException("BAILOUT. Implement the method in the child class");
	}

	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		throw new RuntimeException("BAILOUT. Implement the method in the child class");
	}

	public ExpressionNode getExpressionFromString(String s){
		return (ExpressionNode) getExtendedContext(getContext(s));
	}	

	@SuppressWarnings("unchecked")
	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionContext getContext(String str){
		return (ExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExpressionContext){
				addToContexts((ExpressionContext) ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override public boolean isTerminal(){return false;}
	@Override public boolean isNumber(){return false;}
	@Override public boolean isSymbol(){return isTerminal() && !isNumber();}
	@Override public int getResultSize(){return getLeft().getResultSize();}
	@Override public boolean isLogicalExpression(){return false;}
	@Override public boolean isBinaryExpression(){return false;}
	@Override public boolean isUnaryExpression(){return false;}

	@Override
	public boolean isSimpleExpression(){
		if(isBinaryExpression()){
			return getLeft().isTerminal() && getRight().isTerminal();
		}else if(isUnaryExpression()){
			return getLeft().isTerminal();
		}else{
			return isTerminal();
		}
	}
	@Override
	public String TerminalValue(){
		if(isTerminal()){
			return getFormattedText();
		}else{
			throw new IllegalAccessError("Not a Terminal Node");
		}
	}
	public ExpressionNode getLeft(){throw new UnsupportedOperationException("Only Valid in Expression Extended Context or Not defined in:"+ this.getClass().getName());}
	public ExpressionNode getRight(){throw new UnsupportedOperationException("Only Valid in Expression Extended Context or Right child not present");}

	@Override
	public String getOperator() {
		L.info(this.getClass().getName());
		throw new IllegalArgumentException();
	}
	@Override
	public List<ExpressionNode> getOperands(){
		List<ExpressionNode> ret = new ArrayList<>();
		if(isTerminal()){
			ret.add(this);
		}else if(isUnaryExpression()){
			ret.add(getLeft());
		}else if(isBinaryExpression()){
			ret.add(getLeft());
			ret.add(getRight());
		}
		return ret;
	}

	@Override
	public String summary(){
		return this.getFormattedText();
	}

	public TupleStatementSimplify simplify(){
		TupleStatementSimplify ret = new TupleStatementSimplify();
		if(isSimpleExpression()){
			ret.setSimpleExpression(this);
			return ret;
		}else{
			if(isBinaryExpression()){
				//Left and Right has to be terminals otherwise current expression will not be a simple expression. So terminalize them.
				ExpressionContextExt left = (ExpressionContextExt) getLeft();TupleStatementSimplify l = left.simplify();l = l.terminalize();
				ExpressionContextExt right = (ExpressionContextExt) getRight();TupleStatementSimplify r = right.simplify();r = r.terminalize();
				//Construct simple expression String.
				ret.setSimpleExpression(AbstractBaseExt.getExpression(l.getSimpleExpressionText() + getOperator()+ r.getSimpleExpressionText()));
				//
				ret.getTempAssigns().addAll(l.getTempAssigns());ret.getTempAssigns().addAll(r.getTempAssigns());
				return ret;
			}else if(isUnaryExpression()){
				ExpressionContextExt left = (ExpressionContextExt) getLeft();
				TupleStatementSimplify l = left.simplify();l = l.terminalize();
				//TODO ugly. please change this
				if(this instanceof OfContextExt){
					ret.setSimpleExpression(l.getSimpleExpression());
				}else{
					ret.setSimpleExpression(AbstractBaseExt.getExpression(getOperator()+""+l.getSimpleExpressionText()));
				}
				ret.getTempAssigns().addAll(l.getTempAssigns());
				return ret;
			}else{
				throw new RuntimeException();
			}
		}
	}

	public static String joinByAnd(Stack<ExpressionContextExt> branchStack) {
		StringBuilder sb = new StringBuilder();
		sb.append(branchStack.elementAt(0).getFormattedText());
		for(int i=1;i<branchStack.size();i++){
			sb.append("&&"+branchStack.elementAt(i).getFormattedText());
		}
		return sb.toString();
	}

	public boolean isBitIns(){
		if(!isTerminal()){
			if(getOperator().equals("||")||getOperator().equals("&&")||getOperator().equals("!")){
				return true;
			}
		}else if(getSizeInBits(TerminalValue()) == 1){
			return true;
		}
		return false;	
	}

	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		throw new RuntimeException("BAILOUT. Implement the method in the child class");
	}

	public Expr getExpr(ExpressionNode expNode, Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		if(expNode == null) expNode = getExpressionFromString(getExtendedContext(getContext()).getFormattedText());
		if(expNode.isTerminal()) {
			if(expNode.isNumber()) {
				IntegerContextExt numCtxE = (IntegerContextExt) expNode;
				NumberContextExt numberContextExt = (NumberContextExt)getExtendedContext(((IntegerContext)numCtxE.getContext()).number());
				return solver.getIntSymbol(ByteUtils.parseP4Number(numberContextExt.getFormattedText()).getValue());
			}
			else {
				String var = expNode.TerminalValue();
				if(var.endsWith("isValid")) {
					return solver.getCtx().mkEq(solver.getCtx().mkBool(bs.get(headerValidLocations.get(var))), solver.getCtx().mkBool(true));
				}
				else {
					if(solver.isNewSymbol(var))
						solver.addSymbol(var);
					return solver.getLatestContext(var);
				}
			}
		}
		else {
			ExpressionContextExt exprCtxE = (ExpressionContextExt) expNode;
			return exprCtxE.gatherSymbolsAndConstraints(solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes);
		}
	}

	public BigInteger eval(ExpressionNode expNode, BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		if(expNode == null) expNode = getExpressionFromString(getExtendedContext(getContext()).getFormattedText());
		if(expNode.isTerminal()) {
			if(expNode.isNumber()) {
				IntegerContextExt numCtxE = (IntegerContextExt) expNode;
				NumberContextExt numberContextExt = (NumberContextExt)getExtendedContext(((IntegerContext)numCtxE.getContext()).number());
				return new BigInteger((new Integer(ByteUtils.parseP4Number(numberContextExt.getFormattedText()).getValue())).toString());
			}
			else {
				String var = expNode.TerminalValue();
				if(actionData.containsKey(var))
					return actionData.get(var);
				else if(predicateOffsets.containsKey(var)) 
					return bitVector.get(predicateOffsets.get(var)) ? new BigInteger("1") : new BigInteger("0");
					else if(headerOffsetsAndSizes.containsKey(var)) {
						Integer[] offsetAndSize = headerOffsetsAndSizes.get(var);
						return Utils.toBigInteger(byteVector.get(offsetAndSize[0], offsetAndSize[0] + offsetAndSize[1]));
					}
					else
						throw new RuntimeException("could not evaluate the value of " + var);
			}
		}
		else {
			ExpressionContextExt exprCtxE = (ExpressionContextExt) expNode;
			return exprCtxE.eval(byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
		}
	}

	public static void main(String[] args) {
		ExpressionContextExt e = AbstractBaseExt.getExpression("!hdr.ipv4.isValid()");
		System.out.println(e.isBitIns());
		System.out.println(e.isTerminal());
		System.out.println(e.getClass().getName());
		System.out.println(e.getFormattedText());
	}


	private String getOperatorCode() {

		if (getContext() instanceof EqualContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.EQ);
		}

		if (getContext() instanceof NotEqualContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.NE);
		}

		if (getContext() instanceof GreaterThanContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.GT);
		}

		if (getContext() instanceof LessThanContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.LT);
		}

		if (getContext() instanceof PlusContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.PLUS);
		}

		if (getContext() instanceof BitXorContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.XOR);
		}

		if (getContext() instanceof BitAndContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.AND);
		}

		if (getContext() instanceof BitOrContext) {
			return P416Parser.VOCABULARY.getSymbolicName(P416Parser.OR);
		}

		throw new UnsupportedOperationException("Only Valid in Expression Extended Context or Not defined in:" + this.getClass().getName());
	}

	public boolean isBooleanExpression() {
		return false;
	}

	@Override
	public String getNameString() {
		return getFormattedText();
	}

	@Override
	public boolean isRangeIndexExpression() {
		return false;
	}

	@Override
	public boolean isFunctionCall() {
		return false;
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {

		boolean doTransform = false;
		String expr = "";
		if (isBinaryExpression() && getLeft() != null && getRight() != null
				&& !(getLeft() instanceof OfContextExt || getLeft() instanceof CastContextExt)
				&& !(getRight() instanceof OfContextExt || getRight() instanceof CastContextExt)
				&& !getLeft().isBinaryExpression()
				&& !getRight().isBinaryExpression()) {
			String leftString = ((IExpression) getLeft()).getFormattedText();
			boolean isLeftNumber = getLeft() instanceof IntegerContextExt;
			leftString = isLeftNumber ? "\"" + leftString + "\"" : leftString;

			String rightString = ((IExpression) getRight()).getFormattedText();
			boolean isRightNumber = getRight() instanceof IntegerContextExt;
			rightString = isRightNumber ? "\"" + rightString + "\"" : rightString;

			int size = 0;

			if (!isLeftNumber) {
				Symbol symbol = resolve(leftString);
				size = symbol.getSizeInBits();
			}

			if (!isRightNumber) {
				Symbol symbol = resolve(rightString);
				size = symbol.getSizeInBits();
			}

			if (size <= 32) {
				//upto uint32_t variables, we do not need to transform
				return;
			}

			boolean isFunctionReturnsBoolean = isBooleanExpression();
			String functionName = "Fn" + (isLeftNumber ? "_C" : "_V") + (isFunctionReturnsBoolean ? "_B" : "") + (isRightNumber ? "_C" : "_V");

			expr = functionName +
					"(" +
					leftString +
					", " +
					getOperatorCode() +
					", " +
					rightString +
					", " +
					size +
					")";
			doTransform = true;
		}

		if (doTransform) {
			P416Parser parser = getParser(expr);
			P416Parser.ExpressionContext expressionContext = (ExpressionContext) new PopulateExtendedContextVisitor().visit(parser.expression());
			this.setContext(expressionContext);
		}
	}

	@Override
	public void transformToCPPCode(LinkedHashMap<String, String> map) {
		//do nothing
	}

	@Override
	public void inlineParserStateForNewInstances(Integer instanceNumber, String parseStateName, Integer count, List<String> parseStatesInUnionStack){
		ExpressionContext expressionContext = (ExpressionContext) getContext();
		super.inlineParserStateForNewInstances(instanceNumber, parseStateName, count, parseStatesInUnionStack);
		if(expressionContext instanceof ExprMemberAccessContext){
			String replacedNextOrLast = getExtendedContext(expressionContext).replaceNextOrLast(instanceNumber);
			ExpressionContext expContext = getContext(replacedNextOrLast);
			setContext(expContext);
		}
	}

}
