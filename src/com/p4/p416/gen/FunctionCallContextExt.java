package com.p4.p416.gen;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.p4.p416.gen.P416Parser.ArgumentListContext;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.IFunctionCall;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.Expr;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.FunctionCallContext;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils.Pair;

public class FunctionCallContextExt extends ExpressionContextExt implements IFunctionCall{

	private static final Logger L = LoggerFactory.getLogger(FunctionCallContextExt.class);

	public FunctionCallContextExt(FunctionCallContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FunctionCallContext getContext(String str){
		return (FunctionCallContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof FunctionCallContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ FunctionCallContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public boolean isTerminal() {
		if (getContext() instanceof FunctionCallContext) {
			FunctionCallContext functionCallContext = (FunctionCallContext) getContext();
			if (((ExpressionContextExt)getExtendedContext(functionCallContext.expression())).isTerminal() &&  functionCallContext.argumentList() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getResultSize(){
		if(getFormattedText().endsWith(".isValid()")){
			return 1;
		}
		throw new IllegalStateException("Can't determine size of :"+getFormattedText());
	}

	@Override public String TerminalValue(){
		if(isTerminal()){
			String ret = getFormattedText();
			if(getFormattedText().endsWith(".isValid()")){
				ret = ret.replaceAll("\\(", "");
				ret = ret.replaceAll("\\)", "");
			}
			return ret;
		}
		throw new IllegalArgumentException("Not a terminal");
	}

	//SSA START
	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText();
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	@Override
	public ExpressionNode getLeft() {
		if (getContext() instanceof FunctionCallContext) {
			return (ExpressionNode) getExtendedContext(((FunctionCallContext) getContext()).expression());
		}
		return null;
	}

	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		return super.getExpr(getLeft(),solver,bs, headerValidLocations, headerFieldsOffsetsAndSizes);
	}

	@Override
	public IExpression getExpression() {
		if (getContext() instanceof FunctionCallContext) {
			return (IExpression) getExtendedContext(((FunctionCallContext) getContext()).expression());
		}
		return null;
	}

	@Override
	public boolean isFunctionCall() {
		return true;
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		String expr = "";
		boolean doTransform = false;

		if (getLeft().isTerminal()) {
			if (getLeft().TerminalValue().endsWith("isValid")) {
				expr = getLeft().TerminalValue();
				doTransform = true;
			}
		}

		if (doTransform) {
			P416Parser parser = getParser(expr);
			P416Parser.ExpressionContext expressionContext = (ExpressionContext) new PopulateExtendedContextVisitor().visit(parser.expression());
			this.setContext(expressionContext);
		}
	}

	@Override
	public Type getType(){
		return this;
	}

	@Override
	public String getTypeName(){
		String functionCallSymbolString = getExtendedContext(((FunctionCallContext)getContext()).expression()).getFormattedText();
		if (functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.IS_VALID)){
			Symbol symbol = this.resolve(functionCallSymbolString.substring(0, functionCallSymbolString.lastIndexOf(".")));
			if ("header".equals(symbol.getType().getTypeName())){
				return "BoolType";
			}
		}
		if (functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.SET_VALID) || functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.SET_INVALID)){
			Symbol symbol = this.resolve(functionCallSymbolString.substring(0, functionCallSymbolString.lastIndexOf(".")));
			if ("header".equals(symbol.getType().getTypeName())){
				return null;
			}
		}
		//Other general function calls
		try{
			String typeName = getExtendedContext(((FunctionCallContext)getContext()).expression()).getTypeName();
			if ("BitSizeType".equals(typeName) || "IntSizeType".equals(typeName) || "BoolType".equals(typeName)){
				return typeName;
			}
			Symbol symbol = this.resolve(getExtendedContext(((FunctionCallContext)getContext()).expression()).getFormattedText());
			return symbol.getTypeName();
		}
		catch(SymbolNotDefinedException e){
			return null;
		}
	}

	@Override
	public int getTypeSize(){
		String functionCallSymbolString = getExtendedContext(((FunctionCallContext)getContext()).expression()).getFormattedText();
		if (functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.IS_VALID)){
			Symbol symbol = this.resolve(functionCallSymbolString.substring(0, functionCallSymbolString.lastIndexOf(".")));
			if ("header".equals(symbol.getType().getTypeName())){
				return 1;
			}
		}
		if (functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.SET_VALID) || functionCallSymbolString.endsWith("."+HeaderTypeDeclarationContextExt.SET_INVALID)){
			Symbol symbol = this.resolve(functionCallSymbolString.substring(0, functionCallSymbolString.lastIndexOf(".")));
			if ("header".equals(symbol.getType().getTypeName())){
				return 0;
			}
		}

		//Other general function calls
		try{
			String typeName = getExtendedContext(((FunctionCallContext)getContext()).expression()).getTypeName();
			int typeSize = getExtendedContext(((FunctionCallContext)getContext()).expression()).getTypeSize();
			if ("BitSizeType".equals(typeName) || "IntSizeType".equals(typeName) || "BoolType".equals(typeName)){
				return typeSize;
			}
			Symbol symbol = this.resolve(getExtendedContext(((FunctionCallContext)getContext()).expression()).getFormattedText());
			return symbol.getType().getTypeSize();
		}
		catch(SymbolNotDefinedException e){
			return 0;
		}
	}

	@Override
	public String getSymbolName(){
		return getExtendedContext(((FunctionCallContext)getContext()).expression()).getFormattedText();
	}
}
