package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p4.pktgen.config.cache.CacheConfig;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.model.memory.cache.CacheTable;
import com.p4.pktgen.p4blocks.TableKey;

public class CacheUnit {

	private int cacheId;
	private final int addressibleBits = 33;
	private final int lineSize = 256;
	private final int cacheSize;
	private int fixedPortion;
	
	private int cacheUsed;
	private int nextBaseAddr;
	
	private CacheConfig cacheConfig;
	
	//private Map<Integer, CacheTable> tables;
	
	private Map<Integer, Set<Integer>> tableKsegMap;
	private Map<Integer, Set<Integer>> tableDsegMap;
	private Map<Integer, CacheTable> tablesAllocated;
	private Map<Integer, Integer> ksegsAllocated;
	private Map<Integer, Integer> dsegsAllocated;
	private Map<Integer, Set<Integer>> ksegIdsAllocated;
	private Map<Integer, Set<Integer>> dsegIdsAllocated;
	
	public CacheUnit(int id, CacheConfig config) {
		cacheId = id;
		cacheSize = (int) Math.pow(2,addressibleBits);
		cacheConfig = config;
		//entriesUsed = 0;
		cacheUsed = 0;
		
		fixedPortion = 0;
		nextBaseAddr = 0;
		
		//tablesAllocated = new HashMap<Integer, CacheTable>();
		
		ksegsAllocated = new HashMap<Integer, Integer>();
		dsegsAllocated = new HashMap<Integer, Integer>();
		tablesAllocated = new HashMap<Integer, CacheTable>();
		tableKsegMap = new HashMap<Integer, Set<Integer>>();
		tableDsegMap = new HashMap<Integer, Set<Integer>>();
		ksegIdsAllocated = new HashMap<Integer, Set<Integer>>();
		dsegIdsAllocated = new HashMap<Integer, Set<Integer>>();
	}
	
	public boolean putTableInCache(Integer tableId, List<TableKey> keys, Integer dataWidth, Map<Integer, Integer> tableNumEntries, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId) {
		if(doesTableFitInCache(tableId, keys, dataWidth, ksegIdsExcList, dsegIdsExcList, disjointId, tableNumEntries)) {
			assignKeySeg(tableId, getKeyWidth(keys), ksegIdsExcList, disjointId);
			assignDataSeg(tableId, dataWidth, dsegIdsExcList, disjointId);
			assignTable(tableId, keys, dataWidth, tableNumEntries);
			tablesAllocated.get(tableId).setConfig(tableKsegMap.get(tableId), tableDsegMap.get(tableId));
			return true;
		}
		throw new RuntimeException("unable to place table " + tableId +" in cache");
	}
	
	public boolean doesTableFitInCache(Integer tableId, List<TableKey> keys, Integer dataWidth, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId, Map<Integer, Integer> tableNumEntries) {		
		
		Integer keyWidth = getKeyWidth(keys);
		
		if(ksegIdsAllocated.get(disjointId) == null)
			ksegIdsAllocated.put(disjointId, new HashSet<Integer>());
		if(ksegsAllocated.get(disjointId) == null)
			ksegsAllocated.put(disjointId, 0);
		if(dsegIdsAllocated.get(disjointId) == null)
			dsegIdsAllocated.put(disjointId, new HashSet<Integer>());
		if(dsegsAllocated.get(disjointId) == null)
			dsegsAllocated.put(disjointId, 0);
		
		return isKeySegsAvailable(keyWidth, ksegIdsExcList, disjointId) && isDataSegsAvailable(dataWidth, dsegIdsExcList, disjointId) &&
				isTherePlaceForTable(tableId, keys, dataWidth, tableNumEntries);
	}
	
	private boolean isTherePlaceForTable(Integer tableId, List<TableKey> keys, Integer dataWidth, Map<Integer, Integer> tableNumEntries) {
		
		int numKeySegsRequired = (int) Math.ceil((double) getKeyWidth(keys)/cacheConfig.getKsegWidth());
		int numDataSegsRequired = (int) Math.ceil((double) dataWidth/cacheConfig.getDsegWidth());
		
		int numTableEntries = tableNumEntries != null && tableNumEntries.get(tableId) != null ? tableNumEntries.get(tableId) : cacheConfig.getEntriesPerTable();
		int sizePerEntry = fixedPortion + numKeySegsRequired * cacheConfig.getKsegWidth() + numDataSegsRequired * cacheConfig.getDsegWidth();
		int linesRequired = (int) Math.ceil((double) sizePerEntry/(32*8)); //1 line = 32B
		int totalSizeRequired = numTableEntries * linesRequired * lineSize;
		
		return totalSizeRequired <= (cacheSize - cacheUsed);
	}
	
	public Integer getId() {
		return cacheId;
	}
	
	private Set<Integer> getAvailableDsegs(Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		int dataSegsRequired = (int) Math.ceil((double) dataWidth/cacheConfig.getDsegWidth());
		
		if(cacheConfig.getNumDseg() - dsegsAllocated.get(disjointId) >= dataSegsRequired) {
			
			for(int i=0; i<=(cacheConfig.getNumDseg()-dataSegsRequired); i++) {
				boolean consecutiveAvailable = true;
				Set<Integer> availableDsegs = new HashSet<Integer>();
				for(int j=i; j<(i+dataSegsRequired); j++) {
					
					if(!((dsegIdsExcList != null && !dsegIdsExcList.contains(j) && !dsegIdsAllocated.get(disjointId).contains(j))
							|| (dsegIdsExcList == null && !dsegIdsAllocated.get(disjointId).contains(j)))) {
						consecutiveAvailable = false;
					}
					availableDsegs.add(j);
				}
				if(consecutiveAvailable) {
					//L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", isDataSegmentsAvailable-true");
					return availableDsegs;
				}
			}
			//L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", but consecutive dsegs are not available");
			return null;
		}
		//L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", returning null");
		return null;
	}
	
	private boolean isDataSegsAvailable(Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		return getAvailableDsegs(dataWidth, dsegIdsExcList, disjointId) != null;
	}
	
	private boolean isKeySegsAvailable(Integer keyWidth, Set<Integer> ksegIdsExcList, Integer disjointId) {
		int keySegsRequired = (int) Math.ceil((double) keyWidth/cacheConfig.getKsegWidth());
		
		int minfree = 0;
		for(int i=0; i<cacheConfig.getNumKseg(); i++) {
			if((ksegIdsExcList != null && !ksegIdsExcList.contains(i) && !ksegIdsAllocated.get(disjointId).contains(i))
					|| (ksegIdsExcList == null && !ksegIdsAllocated.get(disjointId).contains(i))) {
				minfree++;
			}
		}
		boolean isKeySegsAvailable = minfree >= keySegsRequired;
		//L.debug("key segments required for key length-"+keyWidth+" is "+keySegsRequired+ ", free key segments-"+minfree+", isKeySegmentAvailble-"+isKeySegsAvailable);
		return isKeySegsAvailable;
	}
	
	private boolean assignKeySeg(Integer tableId, Integer keyWidth, Set<Integer> ksegIdsExcList, Integer disjointId) {
		if(isKeySegsAvailable(keyWidth, ksegIdsExcList, disjointId)) {
			int ksegsRequired = (int) Math.ceil((double) keyWidth/cacheConfig.getKsegWidth());
			Set<Integer> ksegsToBeAllocated = new HashSet<Integer>();
			for(int i=0; i<cacheConfig.getNumKseg(); i++) {
				if((ksegIdsExcList != null && !ksegIdsExcList.contains(i) && !ksegIdsAllocated.get(disjointId).contains(i))
						|| (ksegIdsExcList == null && !ksegIdsAllocated.get(disjointId).contains(i))) {
					ksegsToBeAllocated.add(i);
					if(ksegsToBeAllocated.size() == ksegsRequired)
						break;
				}
			}
			if(ksegsToBeAllocated.size() != ksegsRequired)
				return false;
			for(Integer ksegId: ksegsToBeAllocated)
				ksegIdsAllocated.get(disjointId).add(ksegId);
			tableKsegMap.put(tableId, ksegsToBeAllocated);
			ksegsAllocated.put(disjointId, ksegsAllocated.get(disjointId) + ksegsRequired);
			//L.debug("num key segs assigned to table-"+tableId+" : "+ksegsRequired);
			return true;
		}
		return false;
	}
	
	private boolean assignDataSeg(Integer tableId, Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		Set<Integer> dsegsToBeAllocated = getAvailableDsegs(dataWidth, dsegIdsExcList, disjointId);
		if(dsegsToBeAllocated != null) {
			for(Integer dsegId: dsegsToBeAllocated) 
				dsegIdsAllocated.get(disjointId).add(dsegId);
			tableDsegMap.put(tableId, dsegsToBeAllocated);
			dsegsAllocated.put(disjointId, dsegsAllocated.get(disjointId) + dsegsToBeAllocated.size());
			//L.debug("num data segs assigned to table-"+tableId+" : "+dsegsToBeAllocated.size());
			return true;
		}
		return false;
	}
	
	private boolean assignTable(Integer tableId, List<TableKey> keys, Integer dataWidth, Map<Integer, Integer> tableNumEntries) {
		
		int numKeySegsRequired = (int) Math.ceil((double) getKeyWidth(keys)/cacheConfig.getKsegWidth());
		int numDataSegsRequired = (int) Math.ceil((double) dataWidth/cacheConfig.getDsegWidth());
		
		int numTableEntries = tableNumEntries != null && tableNumEntries.get(tableId) != null ? tableNumEntries.get(tableId) : cacheConfig.getEntriesPerTable();
		int sizePerEntry = fixedPortion + getKeyWidth(keys) + dataWidth;
		int linesRequired = (int) Math.ceil((double) sizePerEntry/(32*8)); //1 line = 32B
		int totalSizeRequired = numTableEntries * linesRequired * lineSize;
		
		CacheTable table = new CacheTable(tableId, nextBaseAddr, linesRequired, numTableEntries, numKeySegsRequired * cacheConfig.getKsegWidth(), numDataSegsRequired * cacheConfig.getDsegWidth());
		tablesAllocated.put(tableId, table);
		nextBaseAddr += totalSizeRequired;
		return true;
	}
	
	public Set<Integer> getTablesAllocated() {
		return tablesAllocated.keySet();
	}
	
	private Integer getKeyWidth(List<TableKey> keys) {
		Integer keyWidth = 0;
		for(TableKey key : keys) {
			keyWidth += key.getSize();
		}
		return keyWidth;
	}
	
	public Set<Integer> getDsegsAllocatedForTable(Integer tableId) {
		return tableDsegMap.get(tableId);
	}
	
	public Set<Integer> getKsegsAllocatedForTable(Integer tableId) {
		return tableKsegMap.get(tableId);
	}
	
	/*public boolean doesTableFitInCache() {
		return false;
	}*/
	
	public void addCacheEntry(Integer tableId, BitSet key, Integer keyLength, BitSet data, Integer dataLength, BitSet mask, BitSet actionPtr) {
		if(tablesAllocated.containsKey(tableId) && tablesAllocated.get(tableId).isNotFull()) {
			tablesAllocated.get(tableId).addEntry(key, keyLength, data, dataLength, mask, actionPtr);
		}
		else {
			throw new RuntimeException("unable to insert key/data into table - " + tableId);
		}
	}
	
	public Map<ControllerType, Integer> getControllersAllocatedForTable() {
		HashMap<ControllerType, Integer> map = new HashMap<ControllerType, Integer>();
		map.put(ControllerType.hash, 1);
		return map;
	}
	
	public void emitKeyAndData(File output) {
		File dir = new File(output.getAbsolutePath() + "/cache");
		File cacheReg = new File(dir.getAbsolutePath() + "/drmt_table_config.txt");
		File cacheData = new File(dir.getAbsolutePath() + "/drmt_table_data.txt");
		try {
			dir.mkdirs();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		for(Integer tableId : tablesAllocated.keySet()) {
			tablesAllocated.get(tableId).emit(cacheReg, cacheData);
		}
	}
}
