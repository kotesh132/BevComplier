package com.p4.p416.gen;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.iface.IParameter;
import com.p4.p416.iface.ITypeRef;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.drmt.parser.P4Direction;
import com.p4.drmt.parser.P4HeaderInst;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parameter;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.InDirectionContext;
import com.p4.p416.gen.P416Parser.InOutDirectionContext;
import com.p4.p416.gen.P416Parser.OutDirectionContext;
import com.p4.p416.gen.P416Parser.ParameterContext;

@SuppressWarnings("unused")
public class ParameterContextExt extends AbstractBaseExt implements IParameter{

	private static final Logger L = LoggerFactory.getLogger(ParameterContextExt.class);

	public ParameterContextExt(ParameterContext ctx) {
		super(ctx);
	}

	@Override
	public  ParameterContext getContext(){
		return (ParameterContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParameterContext getContext(String str){
		return (ParameterContext)new PopulateExtendedContextVisitor().visit(getParser(str).parameter());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParameterContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParameterContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
/*=============================================================================
 * 	Symbol, Scope & Type Interfaces START
 =============================================================================*/
	@Override
	public Type getType()
	{
		Type myType = getType1();
		return myType;
	}

	@Override
	public String getSymbolName()
	{
		ParameterContext ctx = getContext();
		NameContextExt nameContextExt  = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}
	
	@Override
	public String getTypeName()
	{
		ParameterContext ctx = getContext();
		TypeRefContextExt typeRefContextExt  = (TypeRefContextExt)getExtendedContext(ctx.typeRef());
		//return typeRefContextExt.getFormattedText();
		return typeRefContextExt.getType().getTypeName();
	}
	
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
    	this.enclosingScope = enclosingScopeRef.get();
    	if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
    	{
    		//throw new NameCollisionException((String)this.getSymbolKey());
    		L.error("Line:"+getContext().start.getLine()+": Duplicated parameter name: "+this.getContext().name().getText());
    	}
    	this.enclosingScope.add(this);
    	super.defineSymbol(enclosingScopeRef);
	}
	
	
	
	private int bitOffset = -1;
	@Override
	public int getBitOffset(){
		return this.bitOffset;
	}
	
	private int byteOffset = -1;
	@Override
	public int getByteOffset(){
		return this.byteOffset;
	}

	private int alignedByteOffset = -1;
	@Override
	public int getAlignedByteOffset(){
		return this.alignedByteOffset;
	}
	
	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		this.bitOffset = bitOffset.getAndAdd(getSizeInBytes());
	}


	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		this.byteOffset = byteOffset.getAndAdd(getSizeInBytes());
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger byteOffset)
	{
		int inc = alignMem(byteOffset.get(), getSizeInBytes());
		this.alignedByteOffset = byteOffset.addAndGet(inc);
		this.alignedByteOffset = byteOffset.getAndAdd(getSizeInBytes());
	}
	
	@Override
	public MemoryType getMemoryType()
	{
		if ( getSizeInBits() == 1)
		{
			return MemoryType.TypeSrcBit;
		}
		else
		{
			return MemoryType.TypeSrcByte;
		}
	}
	
	
	
/*=============================================================================
* 	Symbol, Scope & Type Interfaces END
=============================================================================*/

	protected void getSt(List<ST> sts){
		/*
		ParameterContext ctx = (ParameterContext) getContext();
		if(ctx.direction() != null && !ctx.direction().getText().equals("")){
			ST toAdd = Utils.getStgGrp().getInstanceOf("variable");
			toAdd.add("name", ctx.name().getText());
			toAdd.add("type", ctx.typeRef().getText());
			sts.add(toAdd);
		}*/
	}

	public P4Parameter addParameter(P4Headers headers) {
		ParameterContext ctx = (ParameterContext) getContext();
		P4Direction dir;
		if(ctx.direction()!=null){
			if(ctx.direction() instanceof InDirectionContext){
				dir = P4Direction.IN;
			}if(ctx.direction()  instanceof InOutDirectionContext){
				dir = P4Direction.INOUT;
			}else if(ctx.direction() instanceof OutDirectionContext){
				dir = P4Direction.OUT;
			}else{
				dir = P4Direction.UNSPECIFIED;
			}
		}else{
			dir = P4Direction.UNSPECIFIED;
		}
		P4HeaderInst hi = new P4HeaderInst(getExtendedContext(ctx.typeRef()).getFormattedText(), getExtendedContext(ctx.name()).getFormattedText());
		headers.getSt().put(getExtendedContext(ctx.name()).getFormattedText(), getExtendedContext(ctx.typeRef()).getFormattedText());
		//System.out.println(ctx.name().extendedContext.getFormattedText() +"  "+ ctx.typeRef().extendedContext.getFormattedText());
		return new P4Parameter(dir,hi);
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getFormattedText();
	}

	@Override
	public ITypeRef getTypeRef() {
		return (ITypeRef) getExtendedContext(getContext().typeRef());
	}
	
	@Override
	public String getDirection() {
		if(getContext().direction() != null)
			return getExtendedContext(getContext().direction()).getFormattedText();
		return null;
	}

}
