package com.p4.pktgen.config.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CacheConfig {

	private Integer addressableMemoryBits;
	private Integer numKseg;
	private Integer numDseg;
	private Integer ksegWidth;
	private Integer dsegWidth;
	private Integer instPtrNumBits;
	private Integer maxTables;
	private Integer missPtr;
	private Integer entriesPerTable;
	
	public CacheConfig(CacheConfig.UnNormalized un) {
		addressableMemoryBits = un.addressableMemoryBits;
		numKseg = un.numKseg;
		numDseg = un.numDseg;
		ksegWidth = un.ksegWidth;
		dsegWidth = un.dsegWidth;
		instPtrNumBits = un.instPtrNumBits;
		maxTables = un.maxTables;
		missPtr = un.missPtr;
		entriesPerTable = un.entriesPerTable;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer addressableMemoryBits;
		public Integer numKseg;
		public Integer numDseg;
		public Integer ksegWidth;
		public Integer dsegWidth;
		public Integer instPtrNumBits;
		public Integer maxTables;
		public Integer missPtr;
		public Integer entriesPerTable;
	}
}
