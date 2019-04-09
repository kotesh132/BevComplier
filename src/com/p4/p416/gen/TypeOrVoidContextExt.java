package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.TypeOrVoidContext;

public class TypeOrVoidContextExt extends AbstractBaseExt {

	public TypeOrVoidContextExt(TypeOrVoidContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeOrVoidContext getContext(){
		return (TypeOrVoidContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeOrVoidContext getContext(String str){
		return (TypeOrVoidContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeOrVoid());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeOrVoidContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeOrVoidContext.class.getName());
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
		if (this.getContext().typeRef()!=null){
			return getExtendedContext(getContext().typeRef()).getTypeName();
		}
		if (this.getContext().VOID()!=null){
			return null;
		}
		if (this.getContext().nonTypeName()!=null){
			return getExtendedContext(getContext().nonTypeName()).getTypeName();
		}
		return "";
	}
	
	@Override
	public int getTypeSize(){
		//We just want the type size of base types as we are interested in validating function return types of base types like bit<x>, int<y>, bool, etc
		if (this.getContext().typeRef()!=null && this.getContext().typeRef().baseType()!=null){
			return getExtendedContext(getContext().typeRef().baseType()).getTypeSize();
		}
		return 0;//for void and non base types
	}
}
