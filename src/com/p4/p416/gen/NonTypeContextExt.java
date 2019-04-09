package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import com.p4.p416.applications.SsaForm;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.NonTypeContext;
import com.p4.utils.Utils.Pair;

public class NonTypeContextExt extends ExpressionContextExt {

	public NonTypeContextExt(NonTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  NonTypeContext getContext(){
		return (NonTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public NonTypeContext getContext(String str){
		return (NonTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NonTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NonTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public boolean isTerminal() {
		return true;
	}
	
	@Override
	public int getResultSize() {
		return resolve(getFormattedText()).getSizeInBits();
	}
	
	@Override
	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		NonTypeContext ctx = getContext();
		String var = getExtendedContext(ctx).getFormattedText();
		if(randomActionParameterValues.containsKey(var))
			return randomActionParameterValues.get(var);
		else if(values.containsKey(var))
			return values.get(var);
		else
			throw new RuntimeException("could not evaluate the value of " + var);
	}
	
	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		return super.eval(null, byteVector, bitVector, headerOffsetsAndSizes, predicateOffsets, actionData);
	}
	
	@Override
	public String getSymbolName()
	{
		return getFormattedText();
	}
	//SSA START
	@Override
	public void setVersions(SsaForm obj ){
		try{
			AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
			this.ssaVersion = defSymbol.getVersion();
			List<Pair<AbstractBaseExt,AbstractBaseExt> > tempList=obj.getRhsVer().get(defSymbol);
			if(tempList!=null){
				tempList.add(new Pair(getExtendedContext(getContext()),obj.getParentCtx()));
			} else {
				tempList=new ArrayList<>();
				tempList.add(new Pair(getExtendedContext(getContext()),obj.getParentCtx()));
				obj.getRhsVer().put(defSymbol, tempList);

			}
			if(obj.hashSetAllPhi.contains(new Pair(defSymbol,this.ssaVersion))){
				obj.hashSetRhsPhi.add(new Pair(defSymbol,this.ssaVersion));
			}
		}catch(SymbolNotDefinedException e) {
			//ignore if resolve cannot find the current symbol
			// and it throws exception.
		}
	}
	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText()+"("+this.ssaVersion+")";
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END
	
	@Override
	public Type getType(){
		return getExtendedContext(getContext().nonTypeName()).getType();
	}
	
	@Override
	public String getTypeName(){
		return getType().getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getType().getTypeSize();
	}
}
