package com.p4.pktgen.model.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.p4.pktgen.config.som.SOMConfig;
import com.p4.pktgen.enums.KeyType;
import com.p4.utils.Utils.Pair;

public class MemoryFactory {

	private Integer sramUniqueId;
	private Integer tcamUniqueId;
	private SOMConfig somConfig;
	private Integer somId;
	
	public MemoryFactory(SOMConfig config, Integer id) {
		somConfig = config;
		sramUniqueId = 0;
		tcamUniqueId = 0;
		somId = id;
	}
	
	public Pair<Pair<Integer,Integer>,List<Memory>> getMemoriesForKey(KeyType keyType, Integer keyWidth, Set<Integer> ksegs, Integer numEntries, boolean isHash) {
		switch(keyType) {
			case exact : return getSramMemories(keyWidth, somConfig.getKsegWidth(), numEntries != null ? numEntries : somConfig.getEntriesPerTable(), somConfig.getSramConfig().getWords(), somConfig.getSramConfig().getBits(), ksegs, isHash, false);
			case lpm :
			case ternary : return getTcamMemories(keyWidth, somConfig.getKsegWidth(), numEntries != null ? numEntries : somConfig.getEntriesPerTable(), somConfig.getTcamConfig().getWords(), somConfig.getTcamConfig().getBits(), ksegs);
		}
		throw new RuntimeException("unsupported key type - " + keyType.name());
	}
	
	public Pair<Pair<Integer,Integer>,List<Memory>> getMemoriesForData(Integer dataWidth, Set<Integer> dsegs, Integer numEntries) {
		return getSramMemories(dataWidth, somConfig.getDsegWidth(), numEntries != null ? numEntries : somConfig.getEntriesPerTable(), somConfig.getSramConfig().getWords(), somConfig.getSramConfig().getBits(), dsegs, false, true);
	}
	
	private Pair<Pair<Integer,Integer>,List<Memory>> getMemories(KeyType keyType, Integer width, Integer segWidth, Integer numEntries, Integer unitDepth, Integer unitWidth, Set<Integer> segIds, boolean isHash, boolean isData) {
		//int x = (int) Math.ceil((double) width/segWidth) * (isHash ? somConfig.getHashConfig().getNumHway() : 1);
		int x = isHash ? (int) Math.ceil((double) width/segWidth) * somConfig.getHashConfig().getNumHway() : (int) Math.ceil((double) width/segWidth);
		int y = (int) Math.ceil((double) numEntries/unitDepth);
		
		List<Memory> memories = new ArrayList<Memory>();
		for(int i=0; i<y; i++) {
			List<Integer> idList = new ArrayList<Integer>(segIds);
			for(int j=0; j<x; j++) {
				switch(keyType) {
					case exact : memories.add(!isData ? new SRAM(unitDepth, unitWidth, somConfig.getKsegWidth(), sramUniqueId, somId) : new SRAM(unitDepth, unitWidth, somConfig.getDsegWidth(), sramUniqueId, somId, idList.get(j))); sramUniqueId++; break;
					case lpm :
					case ternary : memories.add(new TCAM(unitDepth, unitWidth, tcamUniqueId, somId, idList.get(j), width)); tcamUniqueId++; break;
				}
			}
		}
		return Pair.of(Pair.of(y, x), memories);
	}
	
	private Pair<Pair<Integer,Integer>,List<Memory>> getSramMemories(Integer width, Integer segWidth, Integer numEntries, Integer unitDepth, Integer unitWidth, Set<Integer> segIds, boolean isHash, boolean isData) {
		return getMemories(KeyType.exact, width, segWidth, numEntries, unitDepth, unitWidth, segIds, isHash, isData);
	}
	
	private Pair<Pair<Integer,Integer>,List<Memory>> getTcamMemories(Integer width, Integer segWidth, Integer numEntries, Integer unitDepth, Integer unitWidth, Set<Integer> segIds) {
		return getMemories(KeyType.lpm, width, segWidth, numEntries, unitDepth, unitWidth, segIds, false, false);
	}
	
}
