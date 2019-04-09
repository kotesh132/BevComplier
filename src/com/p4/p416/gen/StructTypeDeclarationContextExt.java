package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.iface.IStruct;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.IStructFieldList;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Struct;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.StructTypeDeclarationContext;
import com.p4.packetgen.structures.ClassStructure;
import com.p4.packetgen.structures.Field;
import com.p4.packetgen.utils.HeadersInfo;
import com.p4.packetgen.utils.StatesInfo;

public class StructTypeDeclarationContextExt extends AbstractBaseExt implements IStruct {

	public StructTypeDeclarationContextExt(StructTypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  StructTypeDeclarationContext getContext(){
		return (StructTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StructTypeDeclarationContext getContext(String str){
		return (StructTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).structTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StructTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StructTypeDeclarationContext.class.getName());
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
		StructTypeDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}


	@Override
	public String getTypeName()
	{
		StructTypeDeclarationContext ctx = getContext();
		return ctx.STRUCT().getText();
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
	public void setAlignedByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		return;
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

	@Override
	public int getAlignedSizeInBytes() { return this.alignedSizeInBytes;}


	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/


	@Override
	public void collectInfo(StatesInfo si,HeadersInfo hi){
		StructTypeDeclarationContext ctx = (StructTypeDeclarationContext) getContext();
		ClassStructure cs = new ClassStructure();
		cs.setName(getExtendedContext(ctx.name()).getName());
		List<Field> fields = new ArrayList<Field>();
		Map<String,String> fieldsMap = new HashMap<String,String>();
		getExtendedContext(ctx.structFieldList()).getFields(fields);
		cs.setCtx(ctx);
		for(Field field:fields){
			if(field.isPrefixedType()){
				fieldsMap.put(field.getName(), field.getPrefixedTypeOrDef());
			}else{
				fieldsMap.put(field.getName(), "rand_var< sc_bv < "+field.getType()+" > > ");
			}
		}
		cs.setFieldsMap(fieldsMap);
		hi.add(cs.getName(),cs);
	}

	@Override
	public void buildTypes(P4Headers headers){
		StructTypeDeclarationContext ctx = (StructTypeDeclarationContext) getContext();
		P4Struct hdr = new P4Struct(getExtendedContext(ctx.name()).getFormattedText());
		//System.out.println(ctx.name().getText());
		headers.getLastTypeDecl().push(hdr);
		super.buildTypes(headers);
		hdr = (P4Struct) headers.getLastTypeDecl().pop();
		headers.getAllTypes().add(hdr);
		//System.out.println(ctx.name().getText());
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

	public void getStructs(Map<String, StructTypeDeclarationContextExt> structTypeDeclarations) {
		structTypeDeclarations.put(getNameString(), this);
	}
}
