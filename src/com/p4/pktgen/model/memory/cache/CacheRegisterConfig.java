package com.p4.pktgen.model.memory.cache;

import java.util.BitSet;

import lombok.Data;

@Data
public class CacheRegisterConfig {
	
	private int tableId;
	private int validPos;
	private int defOnMiss;
	private int readOnly;
	private int defaultPtr;
	private int keyPos;
	private int keySize;
	private int datPos;
	private int datSize;
	private int ptrPos;
	private int ptrSize;
	private int rmwPtr;
	private int baseAddr;
	private BitSet keyMap;
	private int qidSeg;
	private BitSet dsegMap;
	private BitSet psegMap;
	private int addrMode;
	private int numEntry;
	private int entrySize;
	private int enableCfg;
}
