package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p4.p416.iface.IPrefixedNameLValue;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.p416.applications.SsaForm;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.PrefixedNameLvalueContext;
import com.p4.utils.Utils.Pair;

public class PrefixedNameLvalueContextExt extends LvalueContextExt implements IPrefixedNameLValue{

	public PrefixedNameLvalueContextExt(PrefixedNameLvalueContext ctx) {
		super(ctx);
	}

	@Override
	public  PrefixedNameLvalueContext getContext(){
		return (PrefixedNameLvalueContext)contexts.get(contexts.size()-1);
	}

	@Override
	public PrefixedNameLvalueContext getContext(String str){
		return (PrefixedNameLvalueContext)new PopulateExtendedContextVisitor().visit(getParser(str).lvalue());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PrefixedNameLvalueContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PrefixedNameLvalueContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public String getSymbolName()
	{
		return getFormattedText();
	}
	

	@Override
	public String getTypeName()
	{
		return getType().getTypeName();
	}
	
	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName())
		&& thatType.getSymbolName().equals(getSymbolName());
	}

	@Override
	public int getSizeInBits()
	{
		return getSizeInBits(getSymbolName());
	}
	
	@Override
	public int getSizeInBytes()
	{
		return getSizeInBytes(getSymbolName());
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		return getAlignedSizeInBytes(getSymbolName());
	}

	@Override
	public int getBitOffset()
	{
		return getBitOffset(getSymbolName());
	}

	@Override
	public int getByteOffset()
	{
		return getByteOffset(getSymbolName());
	}

	@Override
	public int getAlignedByteOffset()
	{
		return getAlignedByteOffset(getSymbolName());
	}


	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/
	//SSA START
	@Override
	public void setVersions(SsaForm obj ){

		AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());

		String str=allHdrFieldsMap.get(defSymbol);
		if(str!=null) {
			mapUsedStructFields.put(defSymbol, str);
		}

		if(obj.getParentCtx() instanceof IfStatementContextExt){
			if(obj.getMinMap().get(defSymbol)==null) {
				obj.getMinMap().put(defSymbol, defSymbol.getVersion());
			}
		} else {
			obj.getMinMap().put(defSymbol, defSymbol.getVersion());
		}

		defSymbol.incVersion();
		this.ssaVersion=defSymbol.getVersion();

		List<Pair<AbstractBaseExt,AbstractBaseExt> > tempList=obj.getAllVer().get(defSymbol);
		if(tempList!=null){
			tempList.add(new Pair(getExtendedContext(getContext()),obj.getAssignCtx()));
		} else {
			ArrayList<Pair<AbstractBaseExt,AbstractBaseExt> > newtempList=new ArrayList<>();
			newtempList.add(new Pair(getExtendedContext(getContext()),obj.getAssignCtx()));
			obj.getAllVer().put(defSymbol, newtempList);
		}
	}

	@Override
	public void populateMaxMap(Map<AbstractBaseExt,Integer> maxMap){
		AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
		maxMap.put(defSymbol,defSymbol.getVersion());
	}

	@Override

	public void markDelete(SsaForm obj){
		// Resolve has problem when the below strings are passed.
		try{
			AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
			List<Pair<AbstractBaseExt,AbstractBaseExt> > allList = obj.getAllVer().get(defSymbol);
			List<Pair<AbstractBaseExt,AbstractBaseExt> > rhsList = obj.getRhsVer().get(defSymbol);

			Comparator<Pair<AbstractBaseExt,AbstractBaseExt>> compare = new Comparator<Pair<AbstractBaseExt,AbstractBaseExt>>() {
				public int compare(Pair<AbstractBaseExt,AbstractBaseExt> p1, Pair<AbstractBaseExt,AbstractBaseExt> p2) {
					if(p1.first().getSymbolName().equals(p2.first().getSymbolName()) &&
							p1.first().getVersion()==p2.first().getVersion()	) return 0;

					int ret = p1.first().getSymbolName().compareTo(p2.first().getSymbolName());
					if(ret!=0) 
						return ret;
					//This below negative value returned should be quite different
					// from the return value of compareTo on strings. A million is an
					// arbitrary number but would work properly until the difference
					// is of magnitude less than 2000. 

					return 1000000*(p1.first().getVersion()-p2.first().getVersion());
				}
			};

			if(rhsList!=null){
				Collections.sort(rhsList,compare);
			}

			if(allList!=null){
				for(int i=0;i<allList.size();i++){
					Pair<AbstractBaseExt,AbstractBaseExt> allPair = allList.get(i);
					if((rhsList==null) || (Collections.binarySearch(rhsList, allPair,compare)<0)){
						if(allPair.second() instanceof AssignmentStatementContextExt){
							AssignmentStatementContextExt assignmentCtx = (AssignmentStatementContextExt)allPair.second();
							if(assignmentCtx!=null){
								Integer version=allPair.first().getVersion();
								boolean dontmark=false;
								Set<Integer> hashSet=obj.getPhiVer().get(defSymbol);
								if(hashSet!=null){
									if(hashSet.contains(version)==true) {
										dontmark=true;
									}
								}
								if(dontmark==false) {
									assignmentCtx.setDel(true);
									try{
										AbstractBaseExt currentSymbol=(AbstractBaseExt) globalScope.resolve(allPair.first().getFormattedText());
										if(currentSymbol==null) {
											assignmentCtx.setContext(null);
										}
									}catch(SymbolNotDefinedException e) {
										// throws exception when symbol is not found which means
										// not at global scope.
										assignmentCtx.setContext(null);
									}
								}
							}
						}
					}
				}
			}
		}catch(SymbolNotDefinedException e) {
			//ignore if resolve cannot find the current symbol
			// and it throws exception.
		}
	}

	@Override
	public void getSSAFormattedText(Params params){
		if(getContext()==null) return;
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr =  getExtendedContext(getContext()).getFormattedText();
		newStr += "("+this.ssaVersion+")";
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END
}
