package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.ExpressionListContext;
import com.p4.p416.gen.P416Parser.SetContext;

public class SetContextExt extends ExpressionContextExt {

	public SetContextExt(SetContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  SetContext getContext(){
		return (SetContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SetContext getContext(String str){
		return (SetContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SetContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SetContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		ExpressionListContext eList =  getContext().expressionList();
		List<String> typeNameList = new ArrayList<String>();
		if (eList!=null){
			for (ExpressionContext eCtx: ((ExpressionListContext)getExtendedContext(eList).getContext()).expression()){
				Type expressionType = getExtendedContext(eCtx).getType();
				typeNameList.add(expressionType.getTypeName());
			}
		}
		return typeNameList.toString();
	}
	
	@Override
	public int getTypeSize(){
		ExpressionListContext eList =  this.getContext().expressionList();
		List<Integer> typeSizeList = new ArrayList<Integer>();
		int tempSum = 0;
		if (eList!=null){
			for (ExpressionContext eCtx: ((ExpressionListContext)getExtendedContext(eList).getContext()).expression()){
				Type expressionType = getExtendedContext(eCtx).getType();
				typeSizeList.add(expressionType.getTypeSize());
				tempSum = tempSum + (expressionType.getTypeSize());
			}
		}
//		return typeSizeList.size();
		return tempSum;
	}
}
