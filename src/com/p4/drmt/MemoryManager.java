package com.p4.drmt;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.p4.p416.applications.*;
import com.p4.utils.Utils;

public class MemoryManager implements com.p4.p416.applications.IMemoryManager {
	final int byteMaxAddress = 2048;
	final int bitMaxAddress = 256;
	MemoryBuffer memoryBuffer;

	Map<Symbol, Utils.Pair<Integer, Integer> > pktByteMap = new HashMap<>();
	Map<Symbol, Utils.Pair<Integer, Integer> > cfgByteMap = new HashMap<>();

	private  MemoryManager(){
		/*Should read the targets properties file to get the memory limits
		 * 
		 */
		memoryBuffer = new MemoryBuffer();

	}

	public static MemoryManager getInstance() {
		return SingletonHelper.INSTANCE;
	}


	public void resetMemoryBuffer()
	{
		memoryBuffer.resetMemoryBuffer();
	}

	public int getAlignSize(int currOffset,int symSize){
		int incAlign=0;
		if(symSize>=3){
			int rem=currOffset%4;
			if(rem!=0){
				incAlign = 4-rem;
			}
		}
		if(symSize==2){
			int rem=currOffset%2;
			if(rem!=0){
				incAlign = 2-rem;
			}
		}
		return incAlign;
	}


	private int alignSymbol(TargetSymbol curSymbol,Map<Symbol, Utils.Pair<Integer, Integer> > addrMap,int curOffset){
		int symSize=curSymbol.getSizeInBytes();
		int inc=getAlignSize(curOffset,symSize);
		curOffset+=inc;
		addrMap.put(curSymbol,new Utils.Pair<>(curOffset,0));
		return curOffset+symSize;
	}


	private int fillAddressMap(Symbol curSymbol,int pktBitOff,int cfgBitOff,int pktByteOff,int cfgByteOff){
		TargetSymbol symbol = (TargetSymbol) curSymbol;
		switch (symbol.getMemoryType()){
			case TypePktBit:
				return Integer.MIN_VALUE;
			case TypeCfgBit:
				return Integer.MIN_VALUE;
			case TypePktByte:
				return alignSymbol(symbol,pktByteMap,pktByteOff);
			case TypeCfgByte:
				return alignSymbol(symbol,cfgByteMap,cfgByteOff);
			default:
				break;
		}
		return Integer.MIN_VALUE;
	}

	@Override
	public void assignAddress(Set<IMemoryRequest> memReqSet) {

		pktByteMap.clear();
		cfgByteMap.clear();

		MemoryRequest[] arrMemReq = memReqSet.toArray(new MemoryRequest[memReqSet.size()]);
		int len = arrMemReq.length;
		Utils.Pair<Integer, Symbol>[] symbolArr = new Utils.Pair[len];
		for (int i = 0; i < len; i++)
			symbolArr[i] = new Utils.Pair(arrMemReq[i].getCurrentSymbol().getSizeInBytes(), arrMemReq[i].getMemRequestSymbol());

		Arrays.sort(symbolArr, new Comparator<Utils.Pair<Integer, Symbol>>() {
			public int compare(Utils.Pair<Integer, Symbol> o1, Utils.Pair<Integer, Symbol> o2) {

				if (o1.first() != o2.first())
					return o1.first().compareTo(o2.first());

				if (o1.second().hashCode() != o2.second().hashCode()) {
					long o1hashCode = o1.second().hashCode();
					long o2hashCode = o2.second().hashCode();

					return (int) (o1hashCode - o2hashCode);
				}
				Symbol sym1 = o1.second();
				Symbol sym2 = o2.second();
				Scope scope1 = sym1.getEnclosingScope();
				Scope scope2 = sym2.getEnclosingScope();
				long scope1hashCode = scope1.hashCode();
				long scope2hashCode = scope2.hashCode();
				return (int) (scope1hashCode - scope2hashCode);
			}
		});

		int offPktBit=0;
		int offCfgBit=0;
		int offPktByte=0;
		int offCfgByte=0;

		for (int i = 0; i < len; i++) {
			int thisSize = symbolArr[i].first();
			TargetSymbol curSymbol = (TargetSymbol)symbolArr[i].second();
			int retVal=fillAddressMap(curSymbol, offPktBit, offCfgBit, offPktByte, offCfgByte);
			if(retVal == Integer.MIN_VALUE) continue;
			switch (curSymbol.getMemoryType()){
				case TypePktBit:
					offPktBit=retVal;
					break;
				case TypeCfgBit:
					offCfgBit=retVal;
					break;
				case TypePktByte:
					offPktByte=retVal;
					break;
				case TypeCfgByte:
					offCfgByte=retVal;
					break;
				default:
					break;
			}
		}
		return;
	}

	private static class SingletonHelper {
		private static final MemoryManager INSTANCE = new MemoryManager();
	}

	public  class MemoryBuffer{
		AtomicInteger pktByteMemory;
		AtomicInteger pktBitMemory;
		AtomicInteger cfgByteMemory;
		AtomicInteger cfgBitMemory;
		public MemoryBuffer()
		{
			this.pktByteMemory = new AtomicInteger();
			this.pktBitMemory = new AtomicInteger();
			this.cfgByteMemory = new AtomicInteger();
			this.cfgBitMemory = new AtomicInteger();
		}
		public void resetMemoryBuffer()
		{
			this.pktByteMemory = new AtomicInteger();
			this.pktBitMemory = new AtomicInteger();
			this.cfgByteMemory = new AtomicInteger();
			this.cfgBitMemory = new AtomicInteger();
		}
		public int memAlloc(TargetSymbol symbol)
		{
			int size = 0;
			int offset = 0;
			switch (symbol.getMemoryType()) {
				case TypePktBit:
					size = symbol.getSizeInBytes();
					offset = this.pktBitMemory.getAndAdd(size);
					return offset;
				case TypeCfgBit:
					size = symbol.getSizeInBytes();
					offset = this.cfgBitMemory.getAndAdd(size);
					return offset;
				case TypePktByte:
				//	size = symbol.getAlignedSizeInBytes();
				//	offset = pktByteMap.get(symbol).first();
				//	System.out.println(symbol.getSymbolName()+" | "+size+"|"+offset);
					// We always need to align the address to multiple of 4.
					// Because setByteOffset and setAlignedByteOffset start from 0
					// and their aligned offsets are relative to start of struct/header (begin=0).
					// Hence the calculations do hold good if we add a multiple of 4 .
                    //
                    int curOff=this.pktByteMemory.get();
					int inc=getAlignSize(curOff,4); // maximum alignment , multiple of 4
					this.pktByteMemory.getAndAdd(inc);
					size = symbol.getAlignedSizeInBytes();
					offset = this.pktByteMemory.getAndAdd(size);
					return offset;
				case TypeCfgByte:
					size = symbol.getSizeInBytes();
					offset = cfgByteMap.get(symbol).first();
					return offset;
				case TypeSrcBit:
				case TypeSrcByte:
					assert (false);
			}
			return Integer.MIN_VALUE;
		}

		public int memBitAlloc()
		{
			int offset = this.pktBitMemory.getAndIncrement();
			return offset;
		}
	}

	@Override
	public int getPktByteOffset() {
		return this.memoryBuffer.pktByteMemory.get();
	}

	@Override
	public int getPktBitOffset() {
		return this.memoryBuffer.pktBitMemory.get();
	}

	@Override
	public void setPktByteOffset(int offset) {
		this.memoryBuffer.pktByteMemory.set(offset);
	}

	@Override
	public void setPktBitOffset(int offset) {
		this.memoryBuffer.pktBitMemory.set(offset);
	}


	@Override
	public int alloc(Symbol symbol) {
		TargetSymbol targetSymbol = (TargetSymbol)symbol;
		int offset = this.memoryBuffer.memAlloc(targetSymbol);
		return offset;
	}

	@Override
	public int allocBit()
	{
		int offset = this.memoryBuffer.memBitAlloc();
		return offset;
	}

}
