package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.applications.IMemoryRequest;
import com.p4.p416.applications.MemoryRequest;
import com.p4.p416.iface.IArgument;
import com.p4.p416.iface.IInstantiation;
import com.p4.p416.iface.ITypeRef;

import lombok.Getter;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.P4HeaderInst;
import com.p4.drmt.parser.P4Headers;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.InstantiationContext;

public class InstantiationContextExt extends AbstractBaseExt implements IInstantiation {

	private static final Logger L = LoggerFactory.getLogger(InstantiationContextExt.class);
	

	public InstantiationContextExt(InstantiationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  InstantiationContext getContext(){
		return (InstantiationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public InstantiationContext getContext(String str){
		return (InstantiationContext)new PopulateExtendedContextVisitor().visit(getParser(str).instantiation());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof InstantiationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ InstantiationContext.class.getName());
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
		InstantiationContext ctx = getContext();
		NameContextExt nameContextExt =  ctx.name().extendedContext;// (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}


	@Override
	public String getTypeName()
	{
		InstantiationContext ctx = getContext();
		TypeRefContextExt typeRefContextExt = (TypeRefContextExt)getExtendedContext(ctx.typeRef());
		return typeRefContextExt.getFormattedText();
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName())
				&& thatType.getSymbolName().equals(getSymbolName());
	}
	
	@Override
	public int getBitOffset()
	{
		return this.bitOffset;
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
	public void setBitOffset(AtomicInteger bitOffset)
	{
		this.bitOffset = bitOffset.get();
	}

	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		this.byteOffset = byteOffset.get();
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger alignedByteOffset)
	{
		this.alignedByteOffset = alignedByteOffset.get();
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
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));

		int bitOffset=0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		int byteOffset=0;
		super.setByteOffset(new AtomicInteger(byteOffset));
		byteOffset=0;
		super.setAlignedByteOffset(new AtomicInteger(byteOffset));

	}
	
	@Override
	public MemoryType getMemoryType()
	{
		InstantiationContext instantiationContext = getContext();
		if ( instantiationContext.argumentList() == null ){ //BAD PATCH
			Type type = getType();
			if ( type.getTypeName().equals("struct") || type.getTypeName().equals("header")
					|| type.getTypeName().equals("header_union") ){
				return MemoryType.TypePktByte;
			}
		}
		return null;
	}

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
	//GL - ControlBlock -START
	public StatementType getStatementType()
	{
		return StatementType.INSTANTIATION_STATEMENT;
	}
	//GL - ControlBlock -END

	@Getter
	private int startingAddress = Integer.MIN_VALUE;

	@Override
	public void preAllocateGlobalAddress(Set<IMemoryRequest> symbolSet) {
		InstantiationContext instantiationContext = getContext();
		if (instantiationContext.argumentList() == null) { //BAD PATCH
			Type type = getType();
			if (type.getTypeName().equals("struct") || type.getTypeName().equals("header")
					|| type.getTypeName().equals("header_union")) {
				symbolSet.add(new MemoryRequest(MemoryRequest.AllocType.ALLOCATE, this.getSymbol()));
			}
		}
	}

	@Override
	public void allocateGlobalAddress(){
		InstantiationContext instantiationContext = getContext();
		if ( instantiationContext.argumentList() == null ){ //BAD PATCH
			Type type = getType();
			if ( type.getTypeName().equals("struct") || type.getTypeName().equals("header")
					|| type.getTypeName().equals("header_union") ){
				this.byteOffset = this.alignedByteOffset = this.memoryManager.alloc(this);
			}
		}
	}

	//GL - ControlBlock -END

	@Override
	public void invokeProgramFlow(List<String> flowNodes){
		InstantiationContext ictx = (InstantiationContext) getContext();
		if(getExtendedContext(ictx.name()).getContext().getText().equals("main")){
			L.info(getExtendedContext(ictx).getFormattedText());
			super.invokeProgramFlow(flowNodes);
		}

	}
	
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		if(probe)
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}

	@Override
	public void getInstantiations(Map<String, InstantiationContext> instantiations){
		InstantiationContext instantiationContext = (InstantiationContext) getContext();
		instantiations.put(getExtendedContext(instantiationContext.name()).getContext().getText(), instantiationContext);
	}
	@Override
	public void buildTypes(P4Headers headers){
		InstantiationContext instantiationContext = (InstantiationContext) getContext();
		if ( instantiationContext.argumentList() == null ){ //BAD PATCH
			headers.getInstances().add(new P4HeaderInst(getExtendedContext(instantiationContext.typeRef()).getFormattedText(), getExtendedContext(instantiationContext.name()).getContext().getText()));
		}
		super.buildTypes(headers);
	}

	@Override
	public List<IArgument> getArguments() {
		List<IArgument> arguments = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IArgument) {
				arguments.add((IArgument) node);
			}
		});
		return arguments;
	}

	@Override
	public ITypeRef getTypeRef() {
		return (ITypeRef) getExtendedContext(getContext().typeRef());

	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getName(); //getContext().name().extendedContext.getName();
	}

	@Override
	public boolean isMain() {
		InstantiationContext context = getContext();
		return ("main".equals(context.name().getText()));
	}
	
	/*@Override
	public void collectControlBlocksToUnroll(List<String> controlBlocksToUnroll){
		if(isMain()){
			for(IArgument argument:getArguments()){
				if(argument.isFunctionCall()){
					IFunctionCall iFunctionCall = argument.getFunctionCall();
					String expression = iFunctionCall.getFormattedText();
					controlBlocksToUnroll.add(expression.substring(0, expression.indexOf("(")));
				}
			}
		}
	}*/
}
