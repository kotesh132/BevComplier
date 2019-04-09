package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.BitSet;

import com.p4.utils.Utils.Pair;

import lombok.Getter;

@Getter
public abstract class Memory {
	
	private Integer depth;
	private Integer width;
	
	public Memory(Integer depth, Integer width) {
		this.depth = depth;
		this.width = width;
	}
	
	public abstract void setId(Integer id);
	public abstract Integer getId();
	public abstract void setAddrRange(Integer start, Integer end);
	public abstract void setTableId(Integer tableId);
	public abstract void setSegId(Integer segId);
	public abstract void addEntry(Integer index, BitSet value);
	public abstract BitSet getEntry(Integer index);
	public abstract String emitModelConfig(Integer index);
	public abstract String getData(Integer index, boolean isLogicalData);
	public abstract String getNonData(Integer index);
	public abstract void emitRTLConfig();
	public abstract Pair<Integer, Integer> getAddrRange();
	public abstract Integer getSegId();
	public abstract void emitKeyAndData(File outputDir);
	public abstract void assignMemId(Pair<Integer, Integer> id);
	public abstract Integer getDataWidth();
}
