package com.p4.p416.gen;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.applications.*;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.FW;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.IntegerContext;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class IntegerContextExt extends ExpressionContextExt {

	private static final Logger L = LoggerFactory.getLogger(IntegerContextExt.class);
	
	private String implicitCastType="";
	private int implicitCastSize=-1;

	public IntegerContextExt(IntegerContext ctx) {
		super(ctx);
	}

	@Override
	public  ExpressionContext getContext(){
		return (ExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public IntegerContext getContext(String str){
		return (IntegerContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof IntegerContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ IntegerContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override public boolean isTerminal() {return true;}
	@Override public boolean isNumber(){return true;}

	protected FW f;
	
	@Override
	public int getResultSize() {

		return this.f.getSize();
	}

	@Override
	public long eval(BitSet byteVector, BitSet bitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		if (getContext() instanceof IntegerContext) {
			IntegerContext ctx = (IntegerContext) getContext();
			String number = getExtendedContext(ctx.number()).getFormattedText();
			number = number.replaceFirst("\\d+w", ""); //could not find proper parser rule that can handle numbers of format 24w0
			return Long.parseLong(number);
		}
		return -1;
	}

	@Override
	public BigInteger eval(BitSet byteVector, BitSet bitVector, Map<String, Integer[]> headerOffsetsAndSizes, Map<String, Integer> predicateOffsets, Map<String, BigInteger> actionData) {
		if (getContext() instanceof IntegerContext) {
			IntegerContext ctx = (IntegerContext) getContext();
			String number = getExtendedContext(ctx.number()).getFormattedText();
			number = number.replaceFirst("\\d+w", ""); //could not find proper parser rule that can handle numbers of format 24w0
			return new BigInteger(number);
		}
		return new BigInteger("-1");
	}

	@Override
	public String getSymbolName()
	{
		return getFormattedText();
	}

	@Override
	public int getSizeInBytes()
	{
		if (!isSemanticChecksPass()){
			return Utils.ceil(this.f.getSize(),8);
		}
		else{
			try{
				return Utils.ceil(this.f.getSize(),8);
			}catch(NullPointerException e){
				L.debug("Number not initialized properly: "+this.getFormattedText());
				return 0;
			}
		}
	}

	@Override
	public int getSizeInBits(){
		return f.getSize();
	}

	@Override
	public int getAlignedSizeInBytes()
	{
		if (!isSemanticChecksPass()){
			return Utils.ceil(this.f.getSize(),8);
		}
		else{
			try{
				return Utils.ceil(this.f.getSize(),8);
			}catch(NullPointerException e){
				L.debug("Number not initialized properly: "+this.getFormattedText());
				return 0;
			}
		}
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
	public void preAllocateGlobalAddress(Set<IMemoryRequest> symbolSet) {
		Symbol symbol =  globalScope.resolve(this.getSymbolName());
		if ( symbol != null)
		{
			int thatByteOffset = ((AbstractBaseExt)symbol).getAlignedByteOffset();
			if (thatByteOffset == Integer.MIN_VALUE ){
				assert(getSymbolName().equals(symbol.getSymbolName()));
				symbolSet.add(new MemoryRequest(IMemoryRequest.AllocType.ALLOCATE,symbol));
			}
			else
			{
				this.alignedByteOffset=this.byteOffset = thatByteOffset;
			}
		}
		else
		{
			symbolSet.add(new MemoryRequest(IMemoryRequest.AllocType.ALLOCATE,symbol));
		}

	}

	@Override
	public void allocateGlobalAddress(){
		Symbol symbol =  globalScope.resolve(this.getSymbolName());
		if ( symbol != null)
		{
			int thatByteOffset = ((AbstractBaseExt)symbol).getAlignedByteOffset();
			if (thatByteOffset == Integer.MIN_VALUE ){
				assert(getSymbolName().equals(symbol.getSymbolName()));
				this.byteOffset = this.alignedByteOffset = memoryManager.alloc(this);
			}
			else
			{
				this.byteOffset =this.alignedByteOffset  = thatByteOffset;
			}
		}
		else
		{
			this.byteOffset = this.alignedByteOffset  = memoryManager.alloc(this);
		}
		if (!constants.containsKey(this.getSymbolName()) )
		{
			constants.put(this.getSymbolName(), this);
		}
		L.debug("allocateGlobalAddress\t" + getFormattedText()+":"+this.byteOffset );
	}

	@Override
	public void defineSymbol(AtomicReference<Scope> enclosingScopeRef) throws NameCollisionException 
	{
		try{
			if (getContext() instanceof IntegerContext) {
				NumberContextExt numberContextExt = (NumberContextExt) getExtendedContext(((IntegerContext)getContext()).number());
				this.f = ByteUtils.parseP4Number(numberContextExt.getFormattedText());
				globalScope.add(this);
			}
		}catch(NumberFormatException e){
			L.error("Line:"+getContext().start.getLine()+": Number can't be handled. "+e.getMessage());
		}
	}

	@Override
	public FW getFW()
	{
		return this.f;
	}


	@Override
	public MemoryType getMemoryType()
	{
		if(this.f.getSize() == 1)
		{
			return MemoryType.TypeCfgBit;
		}
		else
		{
			return MemoryType.TypeCfgByte;
		}
	}
	
	@Override
	public Type getType() {
		return this;
	}
	
	@Override
	public String getTypeName() {
		if (!"".equals(implicitCastType)){
			return implicitCastType;
		}
		return getExtendedContext(((IntegerContext)getContext()).number()).getTypeName();
	}
	
	@Override
	public int getTypeSize() {
		if (!(implicitCastSize==-1)){
			return implicitCastSize;
		}
		return getExtendedContext(((IntegerContext)getContext()).number()).getTypeSize();
	}
	
	public void setTypeName(String typeName){
		implicitCastType = typeName;
	}
	
	public void setTypeSize(int typeSize){
		implicitCastSize = typeSize;
	}

	@Override
	public void transformToCPPCode(LinkedHashMap<String, String> map) {

		ParserRuleContext context = new PopulateExtendedContextVisitor().visit(getParser("\"" + getFormattedText() + "\"").expression());
		addToContexts(context);
		super.transformToCPPCode(map);
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type that){
		return getExtendedContext(((IntegerContext)getContext()).number()).isEquivalenceCompatible(that);
	}
}
