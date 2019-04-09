package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.ITable;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.drmt.semanticchecks.SymbolChecksVisitor;
import com.p4.p416.applications.SsaForm;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.CastContext;
import com.p4.p416.gen.P416Parser.ExprMemberAccessContext;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

import lombok.Getter;
public class ExprMemberAccessContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(ExprMemberAccessContextExt.class);	

	public ExprMemberAccessContextExt(ExprMemberAccessContext ctx) {
		super(ctx);
	}

	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ExpressionContext getContext(String str){
		return (ExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExprMemberAccessContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(getContext().getClass().getSimpleName() + " cannot be casted to "+ ExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public boolean isTerminal() {
		//TODO this is not true & is a shortcut
		return true;
	}

	@Override
	public int getResultSize() {
		return resolve(getFormattedText()).getSizeInBits();
	}

	@Override
	public String getSymbolName()
	{
		return getFormattedText();
	}

	//SSA START
	@Override
	public void setVersions(SsaForm obj){
		try{

			AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
			String str=allHdrFieldsMap.get(defSymbol);
			if(str!=null) {
				mapUsedStructFields.put(defSymbol, str);
			}
			this.ssaVersion = defSymbol.getVersion();
			List<Pair<AbstractBaseExt,AbstractBaseExt> > tempList=obj.getRhsVer().get(defSymbol);
			if(tempList!=null){
				tempList.add(new Pair(getExtendedContext(getContext()),obj.getAssignCtx()));
			}else {
				tempList =new ArrayList<>();
				tempList.add(new Pair(getExtendedContext(getContext()),obj.getAssignCtx()));
				obj.getRhsVer().put(defSymbol, tempList);
			}
			if(obj.hashSetAllPhi.contains(new Pair(defSymbol,this.ssaVersion))){
				obj.hashSetRhsPhi.add(new Pair(defSymbol,this.ssaVersion));
			}
		}catch(SymbolNotDefinedException e) {
			// Ignore if symbol is not found, could be 
			// something in hidden field isValid
		}
	}

	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText() +"("+this.ssaVersion+")" ;
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		String member = getExtendedContext(getContext()).getFormattedText();
		if(values.containsKey(member))
			return values.get(member);
		int offset = headerFieldsOffsetsAndSizes.get(member).first();
		int size = headerFieldsOffsetsAndSizes.get(member).second();
		return Utils.bitSetToLong(byteVector.get(offset, offset+size));
	}

	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		String var = getExtendedContext(getContext()).getFormattedText();
		if (getContext() instanceof ExprMemberAccessContext) {
			ExprMemberAccessContext ctx = (ExprMemberAccessContext) getContext();
			var = getExtendedContext(ctx).getFormattedText();
			if (actionData.containsKey(var))
				return actionData.get(var);
			else if (predicateOffsets.containsKey(var))
				return bitVector.get(predicateOffsets.get(var)) ? new BigInteger("1") : new BigInteger("0");
				else if (headerOffsetsAndSizes.containsKey(var)) {
					Integer[] offsetAndSize = headerOffsetsAndSizes.get(var);
					return Utils.toBigInteger(byteVector.get(offsetAndSize[0], offsetAndSize[0] + offsetAndSize[1]));
				}
		}
		throw new RuntimeException("could not evaluate the value of " + var);

	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		IIRNode controlNode = getParentIRNode();
		while (controlNode != null && !(controlNode instanceof IControl)) {
			controlNode = controlNode.getParentIRNode();
		}

		if (controlNode != null && getContext() instanceof ExprMemberAccessContext) {
			IControl control = (IControl) controlNode;
			ExprMemberAccessContext exprMemberContext = (ExprMemberAccessContext) getContext();
			StringBuilder sb = new StringBuilder();
			String exprMemberAccess = getFormattedText();
			MemberContextExt memberContextExt = (MemberContextExt) getExtendedContext(exprMemberContext.member());
			ExpressionContextExt expressionContextExt = (ExpressionContextExt) getExtendedContext(exprMemberContext.expression());
			if ("apply".equals(memberContextExt.getFormattedText())) {
				Symbol symbol = controlNode.resolve(expressionContextExt.getFormattedText());
				if (symbol instanceof ITable) {
					sb.append(control.getNameString()).append("_").append(exprMemberAccess.replaceFirst(".apply", "_apply"));
				} else {
					sb.append(exprMemberAccess.replaceFirst(".apply", "_apply"));
				}
				P416Parser parser = getParser(sb.toString());
				P416Parser.ExpressionContext expressionContext = (ExpressionContext) new PopulateExtendedContextVisitor().visit(parser.expression());
				this.setContext(expressionContext);
			}
		}
	}

	@Override
	public Type getType(){
		//The Type of x.next and x.last shall be of type x
		String lValueString = getExtendedContext(getContext()).getFormattedText();
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER) || lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueString = lValueString.substring(0, lValueString.lastIndexOf("."));
        }
		if (! ( ((ExprMemberAccessContext)(this.getContext())).expression()  instanceof CastContext)){
			try{
				Symbol symbol = this.resolve(lValueString);
				return symbol.getType();
			}catch(SymbolNotDefinedException e){
				L.error("Line:"+getContext().start.getLine()+": "+lValueString+" can not be resolved");
			}
		}
		return getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getType();
		
		
	}

	@Override
	public String getTypeName(){
    	String lValueString = getExtendedContext(getContext()).getFormattedText();
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER) || lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueString = lValueString.substring(0, lValueString.lastIndexOf("."));
        }
		if (! ( ((ExprMemberAccessContext)(this.getContext())).expression()  instanceof CastContext)){
			Symbol symbol = this.resolve(lValueString);
			return symbol.getTypeName();
		}
		return getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getType().getTypeName();
		
	}

	@Override
	public String replaceNextOrLast(Integer instanceNumber){
		if(getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getContext().getText().equals("next") || getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getContext().getText().equals("last")){
			return getExtendedContext(((ExprMemberAccessContext)getContext()).expression()).getFormattedText()+"_"+instanceNumber;
		}
		return getExtendedContext(getContext()).getFormattedText();
	}

	@Override
	public Boolean hasNextInStateExpression(){
		if(getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getContext().getText().equals("next") || getExtendedContext(((ExprMemberAccessContext)getContext()).member()).getContext().getText().equals("last")){
			return true;
		}else{
			return super.hasNextInStateExpression();
		}
	}
}
