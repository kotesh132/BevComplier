package com.p4.p416.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.applications.*;
import com.p4.p416.applications.IMemoryRequest.AllocType;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.VariableDeclarationContext;

public class VariableDeclarationContextExt extends AbstractBaseExt {
	
	// SSA START
	boolean isDel=false;

	public void setDel(boolean x){
		isDel=x;
	}
	//SSA END

	public VariableDeclarationContextExt(VariableDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  VariableDeclarationContext getContext(){
		return (VariableDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public VariableDeclarationContext getContext(String str){
		return (VariableDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).variableDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof VariableDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ VariableDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	/*=============================================================================
	 * 	Symbol, Scope & Type Interfaces START
	 =============================================================================*/

	@Override
	public String getSymbolName()
	{
		VariableDeclarationContext ctx = getContext();
		NameContextExt nameContextExt  = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}

//	@Override
//	public String getTypeName()
//	{
//		VariableDeclarationContext ctx = getContext();
//		TypeRefContextExt typeRefContextExt  = (TypeRefContextExt)getExtendedContext(ctx.typeRef());
//		return typeRefContextExt.getFormattedText();
//	}
	
	//Added the below because we were getting getTypeName as int<3> instead of IntSizeType with getTypeSize being 3.
	//TODO Remove the above comment later
	@Override
	public String getTypeName()
	{
		return getType1().getTypeName();
	}

	@Override
	public Type getType()
	{
		return getType1();
	}

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
		{
			throw new NameCollisionException((String)this.getSymbolKey());
		}
		this.enclosingScope.add(this);
		super.defineSymbol(enclosingScopeRef);
	}

	private int bitOffset = -1;
	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		this.bitOffset = bitOffset.getAndAdd(getSizeInBits());
	}

	private int byteOffset = -1;
	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		this.byteOffset = byteOffset.getAndAdd(getSizeInBytes());
	}

	private int alignedByteOffset = -1;
	@Override
	public void setAlignedByteOffset(AtomicInteger alignedByteOffset)
	{
		int inc = alignMem(alignedByteOffset.get(), getSizeInBytes());
		this.alignedByteOffset = alignedByteOffset.addAndGet(inc);
		this.alignedByteOffset = alignedByteOffset.getAndAdd(getSizeInBytes());
	}

	@Override
	public int getByteOffset()
	{
		return this.byteOffset;
	}


	@Override
	public int getAlignedByteOffset()
	{
		return this.alignedByteOffset;
	}

	@Override
	public void preAllocateGlobalAddress(Set<IMemoryRequest> symbolSet){
		symbolSet.add(new MemoryRequest(AllocType.ALLOCATE,this.getSymbol()));
	}

	@Override
	public void allocateGlobalAddress(){
		VariableDeclarationContext variableDeclarationContext = getContext();
		this.byteOffset = this.memoryManager.alloc(this);
	}


/*=============================================================================
 	Symbol, Scope & Type Interfaces END
=============================================================================*/



	//GL - ControlBlock -START
	public StatementType getStatementType()
	{
		return StatementType.VARIABLE_DECLARATION_STATEMENT;
	}

	//GL - ControlBlock -END
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}

	//SSA START
	@Override

	public void setVersions(SsaForm obj){
		AbstractBaseExt oldParentCtx = obj.getParentCtx();
		obj.setParentCtx(getExtendedContext(getContext()));
		super.setVersions(obj);
		obj.setParentCtx(oldParentCtx);
	}
	@Override

	public void markDelete(SsaForm obj){
		AbstractBaseExt oldParentCtx = obj.getParentCtx();
		obj.setParentCtx(getExtendedContext(getContext()));
		super.markDelete(obj);
		obj.setParentCtx(oldParentCtx);
	}

	@Override
	public void getSSAFormattedText(Params params){
		if(getContext()==null) return;
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr="";
		if(this.isDel==true){
			newStr="/*DEL*/";
		}
		super.getSSAFormattedText(params);
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END
	
}
