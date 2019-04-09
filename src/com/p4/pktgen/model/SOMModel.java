package com.p4.pktgen.model;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.pktgen.config.cache.CacheConfig;
import com.p4.pktgen.config.som.SOMConfig;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.LayoutType;
import com.p4.pktgen.model.memory.CacheUnit;
import com.p4.pktgen.model.memory.SOMUnit;
import com.p4.pktgen.p4blocks.TableKey;
import com.p4.utils.Utils.Pair;

public class SOMModel {
	
	@Getter
	private List<SOMUnit> soms;
	private CacheUnit cache;
	private boolean isTablesPlaced = false;
	private List<SOMConfig> somConfig;
	private CacheConfig cacheConfig;
	private Set<Integer> tablesInCache;
	
	private static SOMModel instance = null;
	private static final Logger L = LoggerFactory.getLogger(SOMModel.class);
	
	public static void createInstance(List<SOMConfig> somConfig, CacheConfig cacheConfig) {
		if(instance == null)
			instance = new SOMModel(somConfig, cacheConfig);
		else
			L.warn("SOM Model already initialized");
	}
	
	public static SOMModel getInstance() {
		if(instance == null)
			throw new RuntimeException("Error. SOMModel class is not yet initialized");
		return instance;
	}

	private SOMModel(List<SOMConfig> somConfig, CacheConfig cacheConfig) {
		this.somConfig = somConfig;
		this.cacheConfig = cacheConfig;
		soms = new ArrayList<>(this.somConfig.size());
		for(int i=0; i<this.somConfig.size(); i++) {
			soms.add(i, new SOMUnit(i, this.somConfig.get(i)));
		}
		if(cacheConfig != null)
			cache = new CacheUnit(soms.size(), cacheConfig);
		tablesInCache = new HashSet<Integer>();
	}
	
	public Integer findSOMForTable(List<TableKey> keys, Integer dataWidth, Integer tableId) {
		for(SOMUnit som : soms) {
			if(som.doesTableFitInThisSOM(tableId, keys, dataWidth, null, null, 0, null))
				return som.getSomId();
		}
		throw new RuntimeException("unable to fit table into any SOM");
	}
	
	public void addTableToSOM(Integer somId, Integer tableId, List<TableKey> keys, Map<Integer, Integer> tableNumEntries, Integer dataWidth, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId) {
		soms.get(somId).putTableInSOM(tableId, keys, dataWidth, tableNumEntries, ksegIdsExcList, dsegIdsExcList, disjointId);
	}
	
	public void addTableToCache(Integer tableId, List<TableKey> keys, Map<Integer, Integer> tableNumEntries, Integer dataWidth, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId) {
		cache.putTableInCache(tableId, keys, dataWidth, tableNumEntries, ksegIdsExcList, dsegIdsExcList, disjointId);
	}
	
	public void placeTablesInSOMs(Set<List<Integer>> disjointTables, Map<Integer, Pair<List<TableKey>, Integer>> tableKeysAndActionWidths) {
		
		//NOTE: Doing round robin allocation. This strategy may not work if we have to fit in large number of tables in single SOM
		int lastAssignedId = -1;
		int disjointId = 0;
		//boolean useCache = false;
		for(List<Integer> disjointList: disjointTables) {
			Set<Integer> dsegsAssigned = new HashSet<Integer>();
			Set<Integer> ksegsAssigned = new HashSet<Integer>();
			boolean useCache = false;
			for(Integer tableId: disjointList) {
				if(!useCache) {
					int somId = lastAssignedId + 1;
					int count = 0;
					
					while(count < soms.size()) {
						if(somId == soms.size())
							somId = 0;
						if(soms.get(somId).doesTableFitInThisSOM(tableId, tableKeysAndActionWidths.get(tableId).first(), tableKeysAndActionWidths.get(tableId).second(), ksegsAssigned, dsegsAssigned, disjointId, null)) {
							addTableToSOM(somId, tableId, tableKeysAndActionWidths.get(tableId).first(), null, tableKeysAndActionWidths.get(tableId).second(), ksegsAssigned, dsegsAssigned, disjointId);
							lastAssignedId = somId;
							for(Integer ksegId: soms.get(somId).getKsegsAllocatedForTable(tableId)) ksegsAssigned.add(ksegId);
							for(Integer dsegId: soms.get(somId).getDsegsAllocatedForTable(tableId)) dsegsAssigned.add(dsegId);
							break;
						}
						count++;
					}
					if(count == soms.size())
						throw new RuntimeException("unable to fit table into any SOM");
					/*uncomment the below line if cache has to be used*/
					//useCache = true;
				}
				else {
					cache.putTableInCache(tableId, tableKeysAndActionWidths.get(tableId).first(), tableKeysAndActionWidths.get(tableId).second(), null, ksegsAssigned, dsegsAssigned, disjointId);
					tablesInCache.add(tableId);
					useCache = false;
				}
			}
			disjointId++;
		}
		isTablesPlaced = true;
		
		for(SOMUnit somUnit : soms) {
			somUnit.getControllersConfiguration();
			somUnit.updateMemIds();
		}
	}
	
	public void placeTablesInSOMs(Set<List<Integer>> disjointTables, Map<Integer, Pair<List<TableKey>, Integer>> tableKeysAndActionWidths, Map<Integer, Integer> tableNumEntries, Map<Integer, Integer> tableToSomMap) {
		
		int disjointId = 0;
		for(List<Integer> disjointList: disjointTables) {
			Set<Integer> dsegsAssigned = new HashSet<Integer>();
			Set<Integer> ksegsAssigned = new HashSet<Integer>();
			for(Integer tableId: disjointList) {
				//Set<Integer> dsegsAssigned = new HashSet<Integer>();
				//Set<Integer> ksegsAssigned = new HashSet<Integer>();
				int somId = tableToSomMap.get(tableId);
				addTableToSOM(somId, tableId, tableKeysAndActionWidths.get(tableId).first(), tableNumEntries, tableKeysAndActionWidths.get(tableId).second(), ksegsAssigned, dsegsAssigned, disjointId);
				for(Integer ksegId: soms.get(somId).getKsegsAllocatedForTable(tableId)) ksegsAssigned.add(ksegId);
				for(Integer dsegId: soms.get(somId).getDsegsAllocatedForTable(tableId)) dsegsAssigned.add(dsegId);
			}
			disjointId++;
		}
		isTablesPlaced = true;
		
		for(SOMUnit somUnit : soms) {
			somUnit.getControllersConfiguration();
			somUnit.updateMemIds();
		}
	}
	
	public void addTableEntry(Integer somId, Integer tableId, BitSet key, Integer keyLength, BitSet data, Integer dataLength, BitSet mask, BitSet actionPtr) {
		if (tablesInCache.contains(tableId))
			cache.addCacheEntry(tableId, key, keyLength, data, dataLength, mask, actionPtr);
		else
			soms.get(somId == null ? getSomIdOfTable(tableId) : somId).addTableEntry(tableId, key, keyLength, data, dataLength, mask, actionPtr);
	}
	
	public Set<Integer> getKsegsOfTable(Integer tableId) {
		return tablesInCache.contains(tableId) ? cache.getKsegsAllocatedForTable(tableId) : soms.get(getSomIdOfTable(tableId)).getKsegsAllocatedForTable(tableId);
	}
	
	public Set<Integer> getDsegsOfTable(Integer tableId) {
		return tablesInCache.contains(tableId) ? cache.getDsegsAllocatedForTable(tableId) : soms.get(getSomIdOfTable(tableId)).getDsegsAllocatedForTable(tableId);
	}
	
	public Integer getSramsOfTable(Integer tableId) {
		return tablesInCache.contains(tableId) ? 0 : soms.get(getSomIdOfTable(tableId)).getNumSramsOfTable(tableId);
	}
	
	public Integer getTcamsOfTable(Integer tableId) {
		return tablesInCache.contains(tableId) ? 0 : soms.get(getSomIdOfTable(tableId)).getNumTcamsOfTable(tableId);
	}
	
	public Map<ControllerType, Integer> getControllersAllocatedForTable(Integer tableId) {
		return tablesInCache.contains(tableId) ? cache.getControllersAllocatedForTable() : soms.get(getSomIdOfTable(tableId)).getControllersAllocatedForTable(tableId);
	}
	
	public Map<Integer, Pair<Integer, Set<Integer>>> getTableToSomKsegMap() {
		Map<Integer, Pair<Integer, Set<Integer>>> map = new HashMap<Integer, Pair<Integer, Set<Integer>>>();
		for(SOMUnit som : soms) {
			for(Integer tableId: som.getAllocatedTableIds()) {
				map.put(tableId, Pair.of(som.getSomId(), som.getKsegsAllocatedForTable(tableId)));
			}
		}
		if(cache!=null)
			for(Integer tableId: cache.getTablesAllocated()) {
				map.put(tableId, Pair.of(cache.getId(), cache.getKsegsAllocatedForTable(tableId)));
			}
		return map;
	}
	
	public Integer getSomIdOfTable(Integer tableId) {
		for(SOMUnit som : soms) {
			if(som.hasTable(tableId)) 
				return som.getSomId();
		}
		throw new RuntimeException("unable to find table - " + tableId + " in any SOM");
	}
	
	public boolean isTablesPlaced() {
		return isTablesPlaced;
	}
	
	public LayoutType getLayoutType(Integer somId, Integer tableId) {
		return soms.get(somId).getLayoutType(tableId);
	}
	
	public List<List<Integer>> getKeyMemoryLayout(Integer somId, Integer tableId) {
		return soms.get(somId).getKeyMemoryLayout(tableId);
	}
	
	public List<List<Integer>> getDataMemoryLayout(Integer somId, Integer tableId) {
		return soms.get(somId).getDataMemoryLayout(tableId);
	}
	
	public String emitModelConfig() {
		StringBuilder sb = new StringBuilder();
		for(SOMUnit som : soms) {
			sb.append(som.emitModelConfig());
		}
		return sb.toString();
	}
	
	public void emitRTLConfig(File regFile, File dbgFile) {
		for(SOMUnit som : soms) {
			som.emitRTLConfig(regFile);
			som.emitDebugInfo(dbgFile);
		}
	}
	
	public void emitKeyAndData(File output) {
		for(SOMUnit som : soms) {
			som.emitKeyAndData(output);
		}
		if(cache != null)
			cache.emitKeyAndData(output);
	}
	
	public List<SOMConfig> getSomConfig() {
		return somConfig;
	}
	
	public int[][] getDsegMap() {
		int numXtdm = 4; //get this from config file
		int numUnits = soms.size()+(cache==null?0:1);
		int[][] dsegMap = new int[somConfig.get(0).getNumDseg()][numUnits * numXtdm];
		for(int i=0; i<soms.size(); i++) {
			for(Integer tableId : soms.get(i).getAllocatedTableIds()) {
				for(Integer dsegId : soms.get(i).getDsegsAllocatedForTable(tableId)) {
					//System.out.println("somid : " + i + ", table : " + tableId + ", dseg : " + dsegId);
					int somDsegIdx = 0; 
					//quick hack, could have been written in a better way
					for(int j=i*numXtdm; j<i*numXtdm+numXtdm; j++) {
						if(dsegMap[dsegId][j] == 0) {
							somDsegIdx = j;
							break;
						}
					}
					dsegMap[dsegId][somDsegIdx] = tableId;
				}
			}
		}
		for(int i=soms.size(); i<numUnits; i++) {
			for(Integer tableId : cache.getTablesAllocated()) {
				for(Integer dsegId : cache.getDsegsAllocatedForTable(tableId)) {
					int somDsegIdx = 0; 
					for(int j=i*numXtdm; j<i*numXtdm+numXtdm; j++) {
						if(dsegMap[dsegId][j] == 0) {
							somDsegIdx = j;
							break;
						}
					}
					dsegMap[dsegId][somDsegIdx] = tableId;
				}
			}
		}
		return dsegMap;
	}
	
	public int getNumEntriesInTable(int tableId) {
		return soms.get(getSomIdOfTable(tableId)).getNumEntriesInTable(tableId);
	}
}
