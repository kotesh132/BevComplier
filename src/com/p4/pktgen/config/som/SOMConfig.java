package com.p4.pktgen.config.som;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SOMConfig {
	//private Integer numSom;
	private Integer numKseg;
	private Integer numDseg;
	private Integer ksegWidth;
	private Integer dsegWidth;
	private Integer numReadControllers;
	private Integer numWriteControllers;
	private Integer numHashControllers;
	private Integer numCamControllers;
	private Integer numIncrControllers;
	private Integer instPtrNumBits;
	private Integer maxTables;
	private Integer missPtr;
	//private Integer numTcamControllers;
	//private Integer numSramControllers;
	private Integer entriesPerTable;
	private TCAMConfig tcamConfig;
	private SRAMConfig sramConfig;
	private HashConfig hashConfig;
	
	public SOMConfig(SOMConfig.UnNormalized un) {
		//numSom = un.numSom;
		numKseg = un.numKseg;
		numDseg = un.numDseg;
		ksegWidth = un.ksegWidth;
		dsegWidth = un.dsegWidth;
		numReadControllers = un.numReadControllers;
		numWriteControllers = un.numWriteControllers;
		numHashControllers = un.numHashControllers;
		numCamControllers = un.numCamControllers;
		numIncrControllers = un.numIncrControllers;
		//numTcamControllers = un.numTcamControllers;
		//numSramControllers = un.numSramControllers;
		entriesPerTable = un.entriesPerTable;
		instPtrNumBits = un.instPtrNumBits;
		maxTables = un.maxTables;
		missPtr = un.missPtr;
		//intentionally not checking for null
		if(un.tcamConfig != null)
			tcamConfig = new TCAMConfig(un.tcamConfig);
		if(un.sramConfig != null)
			sramConfig = new SRAMConfig(un.sramConfig);
		if(un.hashConfig != null)
			hashConfig = new HashConfig(un.hashConfig);
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		//public Integer numSom;
		public Integer numKseg;
		public Integer numDseg;
		public Integer ksegWidth;
		public Integer dsegWidth;
		public Integer numReadControllers;
		public Integer numWriteControllers;
		public Integer numHashControllers;
		public Integer numCamControllers;
		public Integer numIncrControllers;
		//public Integer numTcamControllers;
		//public Integer numSramControllers;
		public Integer entriesPerTable;
		public Integer instPtrNumBits;
		public Integer maxTables;
		public Integer missPtr;
		public TCAMConfig.UnNormalized tcamConfig;
		public SRAMConfig.UnNormalized sramConfig;
		public HashConfig.UnNormalized hashConfig;
	}
}
