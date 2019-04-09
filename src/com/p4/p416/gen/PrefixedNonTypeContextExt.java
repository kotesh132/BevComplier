package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.SsaForm;
import com.p4.p416.gen.P416Parser.PrefixedNonTypeContext;
import com.p4.utils.Utils.Pair;

public class PrefixedNonTypeContextExt extends ExpressionContextExt {

	public PrefixedNonTypeContextExt(PrefixedNonTypeContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  PrefixedNonTypeContext getContext(){
		return (PrefixedNonTypeContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrefixedNonTypeContext getContext(String str){
		return (PrefixedNonTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PrefixedNonTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PrefixedNonTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public boolean isTerminal() {
		return true;
	}

	//SSA START
	@Override
	public void setVersions(SsaForm obj){

		AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
		this.ssaVersion = defSymbol.getVersion();
		List<Pair<AbstractBaseExt,AbstractBaseExt> > tempList=obj.getRhsVer().get(defSymbol);
		if(tempList!=null){
			tempList.add(new Pair(defSymbol,obj.getParentCtx()));
		}else {
			tempList=new ArrayList<>();
			tempList.add(new Pair(defSymbol,obj.getParentCtx()));
			obj.getRhsVer().put(defSymbol, tempList);
		}
		if(obj.hashSetAllPhi.contains(new Pair(defSymbol,this.ssaVersion))){
			obj.hashSetRhsPhi.add(new Pair(defSymbol,this.ssaVersion));
		}
	}
	//SSA END
}
