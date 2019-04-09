package com.p4.p416.gen;

import com.p4.p416.iface.IHeaderStackType;
import com.p4.p416.iface.ITypeRef;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.TypeRefContext;

public class TypeRefContextExt extends AbstractBaseExt implements ITypeRef {

	public TypeRefContextExt(TypeRefContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeRefContext getContext(){
		return (TypeRefContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeRefContext getContext(String str){
		return (TypeRefContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeRef());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeRefContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeRefContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public String getPrefixedType() {
		return super.getPrefixedType();
	}

	@Override
	public String getSize() {
		return super.getSize();
	}

	@Override
	public boolean isBaseType() {
		return getContext().baseType() != null;
	}

	@Override
	public boolean isSpecializedType() {
		return getContext().specializedType() != null;
	}

	@Override
	public boolean isHeaderStackType() {
		return getContext().headerStackType() != null;
	}

	@Override
	public boolean isTupleType() {
		return getContext().tupleType() != null;
	}

	@Override
	public boolean isTypeName() {
		return getContext().typeName() != null;
	}

	@Override
	public IHeaderStackType getHeaderStackType() {
		if (isHeaderStackType()){
			return (IHeaderStackType) getExtendedContext(getContext().headerStackType());
		}
		return null;
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		if (isBaseType()){
			return getExtendedContext(getContext().baseType()).getTypeName();
		}
		if (isSpecializedType()){
			return getExtendedContext(getContext().specializedType()).getTypeName();
		}
		if (isHeaderStackType()){
			return getExtendedContext(getContext().headerStackType()).getTypeName();
		}
		if (isTupleType()){
			return getExtendedContext(getContext().tupleType()).getTypeName();
		}
		if (isTypeName()){
			return getExtendedContext(getContext().typeName()).getTypeName();
		}
		return this.getFormattedText();
	}
	
	@Override
	public int getTypeSize(){
		if (isBaseType()){
			return getExtendedContext(getContext().baseType()).getTypeSize();
		}
		if (isSpecializedType()){
			return getExtendedContext(getContext().specializedType()).getTypeSize();
		}
		if (isHeaderStackType()){
			return getExtendedContext(getContext().headerStackType()).getTypeSize();
		}
		if (isTupleType()){
			return getExtendedContext(getContext().tupleType()).getTypeSize();
		}
		if (isTypeName()){
			return getExtendedContext(getContext().typeName()).getTypeSize();
		}
		return this.getSizeInBits();
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type that){
		if (this.equals(that)){
			return true;
		}
		
		if (this.getTypeName().equals(that.getTypeName()) && this.getTypeSize() == that.getTypeSize()){
			return true;
		}
		
		return false;
	}
}
