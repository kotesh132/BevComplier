package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.ExpressionListContext;
import com.p4.p416.gen.P416Parser.TupleTypeContext;
import com.p4.p416.gen.P416Parser.TypeArgContext;
import com.p4.p416.gen.P416Parser.TypeArgumentListContext;

public class TupleTypeContextExt extends AbstractBaseExt {

	public TupleTypeContextExt(TupleTypeContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TupleTypeContext getContext(){
		return (TupleTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TupleTypeContext getContext(String str){
		return (TupleTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).tupleType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TupleTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TupleTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType1(){
		return this;
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		TypeArgumentListContext typeArgumentList = this.getContext().typeArgumentList();
		List<TypeArgContext> typeArgList = ((TypeArgumentListContext)getExtendedContext(typeArgumentList).getContext()).typeArg();
		List<String> typeNameList = new ArrayList<String>();
		for (TypeArgContext typeArg : typeArgList){
			typeNameList.add(getExtendedContext(typeArg).getTypeName());
		}
		return typeNameList.toString();
	}
	
	@Override
	public int getTypeSize(){
		TypeArgumentListContext typeArgumentList = this.getContext().typeArgumentList();
		List<TypeArgContext> typeArgList = ((TypeArgumentListContext)getExtendedContext(typeArgumentList).getContext()).typeArg();
		int tempSum = 0;
		List<Integer> typeSizeList = new ArrayList<Integer>();
		for (TypeArgContext typeArg : typeArgList){
			typeSizeList.add(getExtendedContext(typeArg).getTypeSize());
			tempSum = tempSum+getExtendedContext(typeArg).getTypeSize();
		}
//		return typeSizeSet.size();// It should be set actually, not size;
		return tempSum;
	}

	@Override
	public boolean isEquivalenceCompatible(Type that){
		if (this.equals(that)){
			return true;
		}
		
		if (that instanceof SetContextExt){
			TupleTypeContextExt type1 = this;
			SetContextExt type2 = (SetContextExt)that;
			
			Iterator<TypeArgContext> type1Iterator = ((TypeArgumentListContext)getExtendedContext(type1.getContext().typeArgumentList()).getContext()).typeArg().iterator();
			Iterator<ExpressionContext> type2Iterator = ((ExpressionListContext)getExtendedContext(type2.getContext().expressionList()).getContext()).expression().iterator();
			
			while (type1Iterator.hasNext() && type2Iterator.hasNext()){
				TypeArgContextExt type1Next = (TypeArgContextExt) getExtendedContext(type1Iterator.next());
				ExpressionContextExt type2Next = (ExpressionContextExt) getExtendedContext(type2Iterator.next());
				
//				if (type1Next.getTypeSize()!=type2Next.getTypeSize()){
//					return false;
//				}
//				if (!type1Next.getTypeName().equals(type2Next.getTypeName())){
//					return false;
//				}
				if (!type1Next.isEquivalenceCompatible(type2Next)){
					return false;
				}
			}
			return true;
		}
		
		if (that instanceof TupleTypeContextExt){
			TupleTypeContextExt type1 = this;
			TupleTypeContextExt type2 = (TupleTypeContextExt)that;
			
			Iterator<TypeArgContext> type1Iterator = ((TypeArgumentListContext)getExtendedContext(type1.getContext().typeArgumentList()).getContext()).typeArg().iterator();
			Iterator<TypeArgContext> type2Iterator = ((TypeArgumentListContext)getExtendedContext(type2.getContext().typeArgumentList()).getContext()).typeArg().iterator();
			
			while (type1Iterator.hasNext() && type2Iterator.hasNext()){
				TypeArgContextExt type1Next = (TypeArgContextExt) getExtendedContext(type1Iterator.next());
				TypeArgContextExt type2Next = (TypeArgContextExt) getExtendedContext(type2Iterator.next());
				
//				if (type1Next.getTypeSize()!=type2Next.getTypeSize()){
//					return false;
//				}
//				if (!type1Next.getTypeName().equals(type2Next.getTypeName())){
//					return false;
//				}
				
				if (!type1Next.isEquivalenceCompatible(type2Next)){
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
}
