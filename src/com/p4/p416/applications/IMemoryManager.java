package com.p4.p416.applications;

import com.p4.utils.Utils;

import java.util.Map;
import java.util.Set;

public interface IMemoryManager {
	public int alloc(Symbol symbol);
	public int allocBit();
	public int getPktByteOffset();
	public int getPktBitOffset();
	public void setPktByteOffset(int offset);
	public void setPktBitOffset(int offset);
	public void resetMemoryBuffer();
	void assignAddress(Set<IMemoryRequest> memReqSet);
}
