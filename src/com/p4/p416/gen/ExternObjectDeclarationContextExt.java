package com.p4.p416.gen;


import lombok.Getter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class ExternObjectDeclarationContextExt extends ExternDeclarationContextExt {

	@Getter 
	private ExternObjectDeclarationContext ctx;

	public ExternObjectDeclarationContextExt(ExternObjectDeclarationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ExternObjectDeclarationContext getContext(){
		return (ExternObjectDeclarationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExternObjectDeclarationContext getContext(String str){
		return (ExternObjectDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).externDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExternObjectDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExternObjectDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		Scope enclosingScope = enclosingScopeRef.get();
		enclosingScope.add(this);
		
		if (isSemanticChecksPass()){
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
	}
	
	@Override
	public String getSymbolName()
	{
		ExternObjectDeclarationContext ctx = getContext();
		NonTypeNameContextExt nonTypeNameContextExt = (NonTypeNameContextExt)getExtendedContext(ctx.nonTypeName());
		return nonTypeNameContextExt.getFormattedText();
	}

	@Override
	public String getTypeName()
	{
		ExternObjectDeclarationContext ctx = getContext();
		return ctx.EXTERN().getText();
	}
	
	//REVISIT: Semantic Checks is hitting these as additional errors. So using getContext().getText() instead of getFormattedText() for now for better error match
	@Override
	public int getSizeInBits()
	{
		L.error("Not possible to calculate size of Externs" + getContext().getText() );
		return Integer.MIN_VALUE;
	}
	@Override
	public int getSizeInBytes()
	{
		L.error("Not possible to calculate size of Externs" + getContext().getText() );
		return Integer.MIN_VALUE;
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		L.error("Not possible to calculate size of Externs" + getContext().getText() );
		return Integer.MIN_VALUE;
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName()) 
				&& thatType.getSymbolName().equals(getSymbolName());
	}
	
	@Override
	public Type getType()
	{
		return this;
	}
	

	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		return;
	}
	
	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

/*
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
    	{
    		throw new NameCollisionException((String)this.getSymbolKey());
    	}
		this.enclosingScope.add(this);
		Scope newScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(newScope));
		int bitOffset = 0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		int byteOffset=0;
		super.setByteOffset(new AtomicInteger(byteOffset)); //GL Call after setBitOffset.
	}
	*/
	
	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	//GL - ControlBlock -START
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		return;
	}

	//GL - ControlBlock -END
	
	@Override
	public String getNameString() {
		ExternObjectDeclarationContext ctx = getContext();
		return getExtendedContext(ctx.nonTypeName()).getFormattedText();
	}
	
	@Override
	public void getExternObjects(Map<String, ExternObjectDeclarationContextExt> externObjects) {
		externObjects.put(getNameString(), this);
		return;
	}
}
