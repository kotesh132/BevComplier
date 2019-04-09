package com.p4.p416.gen;

import com.p4.p416.iface.IHeaderUnion;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.IStructFieldList;



import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import  com.p4.p416.gen.P416Parser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HeaderUnionDeclarationContextExt extends AbstractBaseExt implements IHeaderUnion {

	public HeaderUnionDeclarationContextExt(HeaderUnionDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  HeaderUnionDeclarationContext getContext(){
		return (HeaderUnionDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public HeaderUnionDeclarationContext getContext(String str){
		return (HeaderUnionDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).headerUnionDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof HeaderUnionDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ HeaderUnionDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public List<IStructFieldList> getStructFieldList() {
		List<IStructFieldList> structFieldsList = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IStructFieldList) {
				structFieldsList.add((IStructFieldList) node);
			}
		});

		return structFieldsList;
	}

	@Override
	public List<IStructField> getStructFields() {
		List<IStructFieldList> structFieldList = getStructFieldList();
		return structFieldList.isEmpty() ? new ArrayList<>() : structFieldList.get(0).getStructFields();
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getName();
	}

	protected int sizeInBits;
	protected int sizeInBytes;
	protected int alignedSizeInBytes;
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		this.enclosingScope.add(this);
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));
		getIsValidStructField().defineSymbol(new AtomicReference<>(localScope));

		int bitOffset=0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		this.sizeInBits = bitOffset;
		int byteOffset=0;
		super.setByteOffset(new AtomicInteger(byteOffset));
		this.sizeInBytes = byteOffset;

		AtomicInteger alignedByteOffset = new AtomicInteger(0);
		super.setAlignedByteOffset(alignedByteOffset);
		this.alignedSizeInBytes=alignedByteOffset.get();	
		
	}

	public IStructField getIsValidStructField(){
		String validFieldInput = "bit<1> isValid ;";
		StructFieldContext structFieldContext = (StructFieldContext)new PopulateExtendedContextVisitor().visit(getParser(validFieldInput).structField());
		return (StructFieldContextExt) getExtendedContext(structFieldContext);
	}

	@Override
	public String getSymbolName()
	{
		HeaderUnionDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}

	@Override
	public String getTypeName()
	{
		HeaderUnionDeclarationContext ctx = getContext();
		return ctx.HEADER_UNION().getText();
	}

	@Override
	public Type getType()
	{
		return this;
	}

}
