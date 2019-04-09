package com.p4.p416.gen;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.applications.Symbol;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.ITypeRef;
import com.p4.utils.Utils;

import lombok.Getter;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.BitStringType;
import com.p4.drmt.parser.FLBitStringType;
import com.p4.drmt.parser.P4HeaderInst;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.VLBitStringType;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.*;
import com.p4.packetgen.structures.Field;

public class StructFieldContextExt extends AbstractBaseExt implements IStructField {

	private static final Logger L = LoggerFactory.getLogger(StructFieldContextExt.class);

	public StructFieldContextExt(StructFieldContext ctx) {
		super(ctx);
	}

	@Override
	public  StructFieldContext getContext(){
		return (StructFieldContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StructFieldContext getContext(String str){
		return (StructFieldContext)new PopulateExtendedContextVisitor().visit(getParser(str).structField());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StructFieldContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StructFieldContext.class.getName());
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
		StructFieldContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}

	@Override
	public Type getType()
	{
		return getType1();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().typeRef()).getTypeSize();
	}


	@Override
	public String getTypeName()
	{
		StructFieldContext ctx = getContext();
		TypeRefContextExt typeRefContextExt = (TypeRefContextExt)getExtendedContext(ctx.typeRef());
		return typeRefContextExt.getType().getTypeName();
		//return typeRefContextExt.getFormattedText();
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName())
				&& thatType.getSymbolName().equals(getSymbolName());
	}

	@Getter
	int bitOffset;
	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		this.bitOffset = bitOffset.getAndAdd(getSizeInBits());
		Symbol sym = (Symbol)getEnclosingScope();
		TypeRefContext trctx = (TypeRefContext)getExtendedContext(getContext().typeRef()).getContext();
		// Ensure that it is of baseType like varbit<32> or bit<32> or bit but not struct or header or
		//header_union aggregate type.
		if(trctx.baseType()!=null)
			fieldsMap.put(new Utils.Pair<>(sym.getSymbolName(),getSymbolName()),new Utils.Pair<>(this.bitOffset, getSymbol().getSizeInBits()));
	}

	@Getter
	int byteOffset;

	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		this.byteOffset = byteOffset.getAndAdd(getSizeInBytes());
	}

	@Getter
	int alignedByteOffset;

	@Override
	public void setAlignedByteOffset(AtomicInteger alignedByteOffset)
	{
		Type type = getParent().getParent().getType();
		if ( type.getTypeName().equals("struct") || type.getTypeName().equals("header")
				|| type.getTypeName().equals("header_union") )
		{
			int inc=alignMem(alignedByteOffset.get(),getAlignedSizeInBytes());
			alignedByteOffset.getAndAdd(inc);
			if(type.getTypeName().equals("struct") || type.getTypeName().equals("header")) {
				this.alignedByteOffset = alignedByteOffset.getAndAdd(getAlignedSizeInBytes());
			} else { // it is a header_union
				/*
				this.alignedByteOffset = alignedByteOffset.getAndAdd(-inc);
				this.alignedSizeInBytes=getAlignedSizeInBytes();
				HeaderUnionDeclarationContextExt ctx= (HeaderUnionDeclarationContextExt)getParent().getParent();
				ctx.setAlignedSizeInBytes(Math.max(alignedSizeInBytes,ctx.getAlignedSizeInBytes()));
				 */
			}
		}
		else {
			int inc=alignMem(alignedByteOffset.get(),getSizeInBytes());
			alignedByteOffset.getAndAdd(inc);
			this.alignedByteOffset = alignedByteOffset.getAndAdd(getSizeInBytes());
		}
	}

	protected int sizeInBits;
	protected int sizeInBytes;
	protected int alignedSizeInBytes;

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		enclosingScope.add(this);

		Symbol sym = (Symbol)getEnclosingScope();
		// Inside structField Context the parentScope always is aggregate
		TypeRefContext trctx = (TypeRefContext) getExtendedContext(getContext().typeRef()).getContext();
		if(trctx.baseType()!=null)
			fieldsMap.put(new Utils.Pair<>(sym.getSymbolName() ,getSymbolName()),new Utils.Pair<>(-1,-1));

	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public void getFields(List<Field> fields){
		StructFieldContext ctx = (StructFieldContext) getContext();
		String name = getExtendedContext(ctx.name()).getName();
		String type = getExtendedContext(ctx.typeRef()).getSize();
		Field f;
		if(type == null){
			type = getExtendedContext(ctx.typeRef()).getPrefixedType();
			f = new Field(name,type);
			f.setPrefixedType(true);
		} else{
			f = new Field(name,type);
			f.setPrefixedType(false);
		}
		fields.add(f);
	}

	public void buildTypes(P4Headers headers){
		StructFieldContext ctx = (StructFieldContext) getContext();
		String name = getExtendedContext(ctx.name()).getFormattedText();
		TypeRefContext trctx = getExtendedContext((TypeRefContext) ctx.typeRef()).getContext();
		if(trctx.baseType()!=null){


			BitStringType bst;
			if(trctx.baseType() instanceof BitTypeContext){
				bst = new FLBitStringType(name, 1);
			}else if(trctx.baseType() instanceof  BitSizeTypeContext){
				BitSizeTypeContext bitSizeTypeContext = (BitSizeTypeContext) trctx.baseType();
				int width = Integer.parseInt(getExtendedContext(bitSizeTypeContext.number()).getFormattedText() );
				bst = new FLBitStringType(name, width);
			}else if(trctx.baseType() instanceof VarBitSizeTypeContext){
				VarBitSizeTypeContext varBitSizeTtypeContext = (VarBitSizeTypeContext) trctx.baseType();
				int width = Integer.parseInt(getExtendedContext(varBitSizeTtypeContext.number()).getFormattedText());
				bst = new VLBitStringType(name, width);
			}else{
				L.warn("WARN: Can't interpret bool or int type yet");
				throw new UnsupportedOperationException("Not yet: int or bool");
			}
			headers.getLastTypeDecl().peek().pushType(bst);
		}else if(trctx.typeName()!=null){
			String typeName = getExtendedContext(trctx.typeName()).getFormattedText();
			// aggregate type like struct,header,header_union. The pair is added for lookup resolution.
			//headers.getSt().put(name,typeName);
			//	if(!typeName.equals("ipv4_t")&& !typeName.equals("ipv6_t"))
			headers.getLastTypeDecl().peek().pushType(new P4HeaderInst(typeName, name) );
		}else{
			L.warn("Can't interpret specialized, headerstack or tuple type declarations\n" + getExtendedContext(trctx).getFormattedText());
		}
		super.buildTypes(headers);
	}

	public MemoryType getMemoryType(){
		if(this.getSizeInBits() > 1){
			return MemoryType.TypePktByte;
		}else{
			return MemoryType.TypePktBit;
		}
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getName();
	}

	@Override
	public ITypeRef getTypeRef() {
		return (ITypeRef) getExtendedContext(getContext().typeRef());
	}


	public void inlineStructFields(List<StructFieldContext> inlinedStructFieldContext, Integer instanceNumber){
		StructFieldContext ctx = (StructFieldContext) getContext();
		StringBuilder sb = new StringBuilder();
		if(((OptAnnotationsContext)getExtendedContext(ctx.optAnnotations()).getContext()).annotations() !=null){
			String text = ((AnnotationContextExt) getExtendedContext(((AnnotationsContext)getExtendedContext(((OptAnnotationsContext)getExtendedContext(ctx.optAnnotations()).getContext()).annotations()).getContext()).annotation(0))).getInlineAnnotationString(instanceNumber);
			sb.append(text+" \n");
		}
		sb.append(getTypeRef().getPrefixedType()+" ");
		sb.append(getNameString()+"_"+instanceNumber+" ");
		sb.append(";\n");
		inlinedStructFieldContext.add(getContext(sb.toString()));
	}
}
