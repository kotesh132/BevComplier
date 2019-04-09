package com.p4.p416.gen;


import com.p4.p416.iface.IHeader;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.IStructFieldList;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.p4.p416.gen.P416Parser.*;
import com.p4.packetgen.structures.ClassStructure;
import com.p4.packetgen.structures.Field;
import com.p4.packetgen.utils.HeadersInfo;
import com.p4.packetgen.utils.StatesInfo;
import com.p4.drmt.parser.P4Header;
import com.p4.drmt.parser.P4Headers;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class HeaderTypeDeclarationContextExt extends AbstractBaseExt implements IHeader {

	private static final Logger L = LoggerFactory.getLogger(HeaderTypeDeclarationContextExt.class);
	
	public static final String IS_VALID = "isValid";

	public static final String SET_VALID = "setValid";

	public static final String SET_INVALID = "setInvalid";
	

	private StructFieldContextExt isValidContextExt = null;

	public HeaderTypeDeclarationContextExt(HeaderTypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  HeaderTypeDeclarationContext getContext(){
		return (HeaderTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public HeaderTypeDeclarationContext getContext(String str){
		return (HeaderTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).headerTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof HeaderTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ HeaderTypeDeclarationContext.class.getName());
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
		HeaderTypeDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}
	

	@Override
	public String getTypeName()
	{
		HeaderTypeDeclarationContext ctx = getContext();
		return ctx.HEADER().getText();
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
	public int getTypeSize(){
		int combinedTypeSize=0;
		if (this.getContext().structFieldList()!=null){
		List<StructFieldContext> structFields = this.getContext().structFieldList().structField();
		for (StructFieldContext structField:structFields){
			if ("VarBitSizeType".equals(getExtendedContext(structField).getTypeName())){
				//TODO: RECHECK if this may be child of anything else. If that is the case, we need to propagate the same
				return VarBitSizeTypeContextExt.SIZE_INDETERMINATE;
			}
			combinedTypeSize = combinedTypeSize+getExtendedContext(structField).getTypeSize();
		}
		}
		return combinedTypeSize;
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


	protected int sizeInBits;
	protected int sizeInBytes;
	protected int alignedSizeInBytes;
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
    	this.sizeInBits = bitOffset;
    	int byteOffset=0;
    	super.setByteOffset(new AtomicInteger(byteOffset));
    	this.sizeInBytes = byteOffset;

		AtomicInteger alignedByteOffset = new AtomicInteger(0);
		super.setAlignedByteOffset(alignedByteOffset);
		this.alignedSizeInBytes=alignedByteOffset.get();
		getIsValidStructField().defineSymbol(new AtomicReference<>(localScope));
    }

	public IStructField getIsValidStructField() {
		String validFieldInput = "bit<1> isValid ;";
		if (isValidContextExt == null) {
			StructFieldContext isValidContext = (StructFieldContext) new PopulateExtendedContextVisitor().visit(getParser(validFieldInput).structField());
			isValidContextExt = (StructFieldContextExt) getExtendedContext(isValidContext);
		}
		return isValidContextExt;
	}
	
	@Override
	public MemoryType getMemoryType(){
    	return MemoryType.TypePktByte;
    }
	
	@Override
	public int getValidBitOffset()
	{
		return this.validBitOffset;
	}


	@Override
	public int getAlignedSizeInBytes() { return this.alignedSizeInBytes;}
	
	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/
	@Override
	public void collectInfo(StatesInfo si,HeadersInfo hi){
		HeaderTypeDeclarationContext ctx = (HeaderTypeDeclarationContext) getContext();
		ClassStructure cs = new ClassStructure();
		cs.setName(getExtendedContext(ctx.name()).getName());
		List<Field> fields = new ArrayList<Field>();
		Map<String,String> fieldsMap = new LinkedHashMap<String,String>();
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
		HeaderTypeDeclarationContext ctx = (HeaderTypeDeclarationContext) getContext();
		P4Header hdr = new P4Header(getExtendedContext(ctx.name()).getFormattedText());
		//System.out.println(ctx.name().getText());
		headers.getLastTypeDecl().push(hdr);
		super.buildTypes(headers);
		hdr = (P4Header) headers.getLastTypeDecl().pop();
		headers.getAllTypes().add(hdr);
		//System.out.println(ctx.name().getText());
	}
	
	@Override
	public void allocateGlobalAddress(){
		this.validBitOffset = memoryManager.allocBit();
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
	
	public void getHeaders(Map<String, HeaderTypeDeclarationContextExt> headerTypeDeclarations) {
		headerTypeDeclarations.put(getNameString(), this);
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type that){
		HeaderTypeDeclarationContextExt type1 = this;
		if (that instanceof ArrayIndexContextExt){
			ArrayIndexContextExt type2 = (ArrayIndexContextExt)that;
			Symbol symbol = this.resolve(type2.getTypeName());
			if (type1.getTypeName().equals(symbol.getTypeName())){
				return true;
			}
		}
		return false;
	}
}
