package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

import com.p4.pktgen.config.som.SOMConfig;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.KeyType;
import com.p4.pktgen.enums.LayoutType;
import com.p4.pktgen.model.controller.CAMController;
import com.p4.pktgen.model.controller.HashController;
import com.p4.pktgen.model.controller.ReadController;
import com.p4.pktgen.model.controller.SRAMController;
import com.p4.pktgen.p4blocks.TableKey;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class SOMUnit {
	
	@Getter private Integer somId;
	
	private SOMConfig somConfig;
	private Map<Integer, Integer> ksegsAllocated;
	private Map<Integer, Integer> dsegsAllocated;
	private Integer readControllersAllocated;
	private Integer writeControllersAllocated;
	private Integer hashControllersAllocated;
	private Integer camControllersAllocated;
	private Integer sramsAllocated;
	private Integer tcamsAllocated;
	private Integer reservedSrams;
	
	private List<SRAM> srams;
	private List<TCAM> tcams;
	
	private Map<Integer, Pair<Integer, Integer>> tableKeyBoundariesMap;
	private Map<Integer, Pair<Integer, Integer>> tableDataBoundariesMap;
	private Map<Integer, Set<Integer>> tableKsegMap;
	private Map<Integer, Set<Integer>> tableDsegMap;
	private Map<Integer, List<Memory>> tableKeyMemoryMap;
	private Map<Integer, List<Memory>> tableDataMemoryMap;
	private Map<Integer, KeyType> tableKeyTypeMap;
	private Map<Integer, Map<ControllerType, Integer>> tableControllerMap;
	private Map<Integer, Integer> tableNumEntriesMap;
	
	private Map<Integer, Table> tablesAllocated;
	private Map<Integer, Set<Integer>> ksegIdsAllocated;
	private Map<Integer, Set<Integer>> dsegIdsAllocated;
	private Set<Integer> hashTableList;
	
	private List<CAMController> camControllers;
	private List<HashController> hashControllers;
	private List<ReadController> readControllers;
	private List<SRAMController> sramControllers;
	private MemoryFactory memoryFactory;
	
	private TCAMGrid tcamGrid;
	//private Map<Integer,HashTable> hashTable;
	
	private final Integer numWordSideSramsForDirectAddressing = 4;
	
	private Logger L = LoggerFactory.getLogger(SOMUnit.class);
	
	public SOMUnit(Integer somId, SOMConfig somConfig) {
		this.somConfig = somConfig;
		this.somId = somId;
		ksegsAllocated = new HashMap<Integer, Integer>();
		dsegsAllocated = new HashMap<Integer, Integer>();
		readControllersAllocated = 0;
		writeControllersAllocated = 0;
		hashControllersAllocated = 0;
		camControllersAllocated = 0;
		sramsAllocated = 0;
		tcamsAllocated = 0;
		reservedSrams = 0;
		
		tablesAllocated = new HashMap<Integer, Table>();
		tableKsegMap = new HashMap<Integer, Set<Integer>>();
		tableDsegMap = new HashMap<Integer, Set<Integer>>();
		ksegIdsAllocated = new HashMap<Integer, Set<Integer>>();
		dsegIdsAllocated = new HashMap<Integer, Set<Integer>>();
		hashTableList = new HashSet<Integer>();
		tableKeyTypeMap = new HashMap<Integer, KeyType>();
		tableControllerMap = new HashMap<Integer, Map<ControllerType, Integer>>();
		tableKeyBoundariesMap = new HashMap<Integer, Pair<Integer, Integer>>();
		tableDataBoundariesMap = new HashMap<Integer, Pair<Integer, Integer>>();
		//tableNumEntriesMap = new HashMap<Integer, Integer>();
		
		camControllers = new ArrayList<CAMController>();
		readControllers = new ArrayList<ReadController>();
		hashControllers = new ArrayList<HashController>();
		sramControllers = new ArrayList<SRAMController>();
		
		memoryFactory = new MemoryFactory(somConfig, somId);
		
		if(somConfig.getTcamConfig() != null)
			tcamGrid = new TCAMGrid(somConfig.getTcamConfig().getNumRow(), somConfig.getTcamConfig().getNumCol(), somId, tableKsegMap, somConfig.getTcamConfig().getWords(), somConfig.getMaxTables());
		
		srams = new ArrayList<SRAM>();
		tcams = new ArrayList<TCAM>();
	}
	
	public boolean putTableInSOM(Integer tableId, List<TableKey> keys, Integer dataWidth, Map<Integer, Integer> tableNumEntries, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId) {
		
		
		
		if(doesTableFitInThisSOM(tableId, keys, dataWidth, ksegIdsExcList, dsegIdsExcList, disjointId, tableNumEntries)) {
			L.debug("table-" + tableId + " fits in som-"+somId+", allocate resources");
			KeyType keyType = getKeyType(keys);
			Integer keyWidth = getKeyWidth(keys);
			
			tableKeyTypeMap.put(tableId, keyType);
			
			assignKeySeg(tableId, keyWidth, ksegIdsExcList, disjointId);
			assignDataSeg(tableId, dataWidth, dsegIdsExcList, disjointId);
			assignController(keyType, keyWidth, tableId);
			assignTableToSOM(tableId, keyType, keyWidth, dataWidth);
			
			return true;
		}
		else {
			throw new RuntimeException("unable to fit table - " + tableId + " into SOM");
		}
	}
	
	public boolean doesTableFitInThisSOM(Integer tableId, List<TableKey> keys, Integer dataWidth, Set<Integer> ksegIdsExcList, Set<Integer> dsegIdsExcList, Integer disjointId, Map<Integer, Integer> tableNumEntries) {		
		KeyType keyType = getKeyType(keys);
		Integer keyWidth = getKeyWidth(keys);
		
		tableNumEntriesMap = tableNumEntries;
		if(ksegIdsAllocated.get(disjointId) == null)
			ksegIdsAllocated.put(disjointId, new HashSet<Integer>());
		if(ksegsAllocated.get(disjointId) == null)
			ksegsAllocated.put(disjointId, 0);
		if(dsegIdsAllocated.get(disjointId) == null)
			dsegIdsAllocated.put(disjointId, new HashSet<Integer>());
		if(dsegsAllocated.get(disjointId) == null)
			dsegsAllocated.put(disjointId, 0);
		
		return isKeySegsAvailable(keyWidth, ksegIdsExcList, disjointId) && isDataSegsAvailable(dataWidth, dsegIdsExcList, disjointId) 
				&& isControllerAvailable(keyType, keyWidth) && isTherePlaceForKey(keyType, keyWidth, tableId)
				&& isTherePlaceForData(dataWidth, tableId);
	}
	
	private Integer getKeyWidth(List<TableKey> keys) {
		Integer keyWidth = 0;
		for(TableKey key : keys) {
			keyWidth += key.getSize();
		}
		return keyWidth;
	}
	
	private KeyType getKeyType(List<TableKey> keys) {
		boolean exact = false;
		boolean lpm = false;
		boolean ternary = false;
		
		for(TableKey key : keys) {
			if(key.getKeyType().equals(KeyType.exact))
				exact = true;
			else if(key.getKeyType().equals(KeyType.lpm))
				lpm = true;
			else if(key.getKeyType().equals(KeyType.ternary))
				ternary = true;
			else
				throw new RuntimeException("unsupported key type");
		}
		
		if(exact && (lpm || ternary))
			throw new RuntimeException("Both exact and lpm/ternary combination of keys for a given table is not supported");
		
		if(exact)
			return KeyType.exact;
		else if(lpm && !ternary)
			return KeyType.lpm;
		else
			return KeyType.ternary;
	}
	
	private boolean isHashRequired(Integer keyWidth) {
		if(somConfig.getSramConfig() != null) {
			//double sramsForDirectAddressing = somConfig.getSramConfig().getWords() * 3; //3 is just a number. if more than 3 srams are required then better do a hash
			int bitsRequiredForDirectAddressing = Utils.lg(somConfig.getSramConfig().getWords() * numWordSideSramsForDirectAddressing);
			boolean hashRequired = keyWidth > bitsRequiredForDirectAddressing ? true : false;
			if(hashRequired && somConfig.getHashConfig() == null)
				throw new RuntimeException("Hashing is required for given table specification but hashconfig is missing in the config file");
			if(hashRequired)
				L.debug("key width is " + keyWidth + " and bits required for direct addressing is " + bitsRequiredForDirectAddressing + ", so hash table implementation is required");
			else
				L.debug("key width is " + keyWidth + " and bits required for direct addressing is " + bitsRequiredForDirectAddressing + ", so hash table implementation is not required");
			return hashRequired;
		}
		L.debug("sram config is not provided in pgen config");
		return false;
	}
	
	private boolean isControllerAvailableForExactKeyType(Integer keyWidth) {
		if(somConfig.getSramConfig() != null) {
			return isHashRequired(keyWidth) ? isHashControllerAvailable() : isReadControllerAvailable();
		}
		L.debug("sram config is not provided in pgen config");
		return false;
	}
	
	private boolean assignControllerForExactKeyType(Integer keyWidth, Integer tableId) {
		if(somConfig.getSramConfig() != null) {
			return isHashRequired(keyWidth) ? assignHashController(tableId) : assignReadController(tableId);
		}
		L.debug("sram config is not provided in pgen config");
		return false;
	}
	
	private boolean isControllerAvailable(KeyType keyType, Integer keyWidth) {
		switch(keyType) {
			case exact : return isControllerAvailableForExactKeyType(keyWidth);
			case lpm : 
			case ternary : return isCamControllerAvailable();
			default : throw new RuntimeException("unsupported key type");
		}
	}
	
	private boolean assignController(KeyType keyType, Integer keyWidth, Integer tableId) {
		switch(keyType) {
			case exact : return assignControllerForExactKeyType(keyWidth, tableId);
			case lpm : 
			case ternary : return assignCamController(tableId);
			default : throw new RuntimeException("unsupported key type");
		}
	}
	
	private void updateTcamAvailability(Integer tableId, Integer rows, Integer cols) {
		tcamGrid.updateTcamAvailability(tableId, rows, cols);
	}
	
	private boolean isTcamAvailable(Integer rows, Integer cols) {
		return tcamGrid.isTcamAvailable(rows, cols);
	}
	
	public void getControllersConfiguration() {
		if(tcamGrid != null) {
			tcamGrid.getControllersConfiguration();
		}
		updateControllers();
	}
	
	public void updateMemIds() {
		if(tcamGrid != null && tcamGrid.getTableTcamAllocation() != null) {
			Map<Integer, List<List<Pair<Integer, Integer>>>> tableTcamMap = tcamGrid.getTableTcamAllocation();
			for(Integer tableId : tablesAllocated.keySet()) {
				if(tableTcamMap.get(tableId) != null)
					tablesAllocated.get(tableId).updateMemIds(tableTcamMap.get(tableId));
			}
		}
	}
	
	private boolean isThereTcamForKey(Integer keyWidth, Integer tableId) {
		if(somConfig.getTcamConfig() != null) {
			return isTcamAvailable((int) Math.ceil((double) (tableNumEntriesMap != null && tableNumEntriesMap.get(tableId) != null ? tableNumEntriesMap.get(tableId) : somConfig.getEntriesPerTable())/somConfig.getTcamConfig().getWords()), (int) Math.ceil((double) keyWidth/somConfig.getTcamConfig().getBits()));
		}
		L.debug("tcam config is not provided in pgen config");
		return false;
	}
	
	private boolean isTherePlaceForExactKey(Integer keyWidth, Integer tableId) {
		int numSramsRequiredForKey = numSramsRequiredForKey(keyWidth, tableId);
		boolean isTherePlaceForKey = ((somConfig.getSramConfig().getNumSram() - sramsAllocated) >= numSramsRequiredForKey) && ((somConfig.getSramConfig().getNumSram() - reservedSrams) >= numSramsRequiredForKey);
		reservedSrams += numSramsRequiredForKey;
		L.debug("total srams in som"+somId+" is "+somConfig.getSramConfig().getNumSram()+", total srams already allocated-"+sramsAllocated+", number of srams required for key-"+numSramsRequiredForKey+", is there place for exact key for table-" +tableId+" : " + isTherePlaceForKey);
		return isTherePlaceForKey;
	}
	
	private boolean isTherePlaceForKey(KeyType keyType, Integer keyWidth, Integer tableId) {
		switch(keyType) {
			case exact : return isTherePlaceForExactKey(keyWidth, tableId);
			case lpm : 
			case ternary : return isThereTcamForKey(keyWidth, tableId);
			default : throw new RuntimeException("unsupported key type");
		}
	}
	
	public void addTableEntry(Integer tableId, BitSet key, Integer keyLength, BitSet data, Integer dataLength, BitSet mask, BitSet actionPtr) {
		if(tablesAllocated.containsKey(tableId) && tablesAllocated.get(tableId).isNotFull()) {
			tablesAllocated.get(tableId).addEntry(key, keyLength, data, dataLength, mask, actionPtr);
		}
		else {
			throw new RuntimeException("unable to insert key/data into table - " + tableId);
		}
	}
	
	private Integer numSramsRequiredForKey(Integer keyWidth, Integer tableId) {
		if(isHashRequired(keyWidth)) {
			return (int) (Math.ceil((double) keyWidth/somConfig.getKsegWidth()) * somConfig.getHashConfig().getNumHway() * (Math.pow(2,somConfig.getHashConfig().getMaskLength())/somConfig.getSramConfig().getWords()));
		} 
		else {
			//direct addressing. so no key is required
			return 0;
		}
	}
	
	private void assignTableToSOM(Integer tableId, KeyType keyType, Integer keyWidth, Integer dataWidth) {
		boolean isHashTable = keyType.equals(KeyType.exact) && isHashRequired(keyWidth);
		boolean isDirectAddressing = keyType.equals(KeyType.exact) && !isHashRequired(keyWidth);
		int numTableEntries = isDirectAddressing ? (int) Math.pow(2, keyWidth) : (isHashTable ? (int) Math.pow(2,somConfig.getHashConfig().getMaskLength()) : ((tableNumEntriesMap != null && tableNumEntriesMap.get(tableId) != null ? tableNumEntriesMap.get(tableId) : somConfig.getEntriesPerTable())));
		Pair<Pair<Integer,Integer>,List<Memory>> k = isDirectAddressing ? null : memoryFactory.getMemoriesForKey(keyType, keyWidth, tableKsegMap.get(tableId), numTableEntries, isHashTable);
		Pair<Pair<Integer,Integer>,List<Memory>> d = memoryFactory.getMemoriesForData(dataWidth, tableDsegMap.get(tableId), numTableEntries);
		Table table = new Table(somConfig, tableId, getKeyMemoryIndex(keyType), getDataMemoryIndex(), k, d, numTableEntries, isHashTable, isDirectAddressing);
		if(isHashTable)
			hashTableList.add(tableId);
		tablesAllocated.put(table.getId(), table);
		if(!isDirectAddressing)
			updateKeyMemoryIndex(tableId, keyType, k.first().first(), k.first().second(), keyWidth);
		updateDataMemoryIndex(tableId, d.first().first(), d.first().second(), d.second());
	}
	
	private Integer getKeyMemoryIndex(KeyType keyType) {
		switch(keyType) {
			case exact : return sramsAllocated;
			case lpm : 
			case ternary : return tcamsAllocated;
			default : throw new RuntimeException("unsupported key type");
		}
	}
	
	private void updateKeyMemoryIndex(Integer tableId, KeyType keyType, Integer rows, Integer cols, Integer keyWidth) {
		tableKeyBoundariesMap.put(tableId, Pair.of(rows, cols));
		switch(keyType) {
			case exact : sramsAllocated += numSramsRequiredForKey(keyWidth, tableId); break;
			case lpm : 
			case ternary : updateTcamAvailability(tableId, rows, cols); tcamsAllocated +=  2 * rows * cols; break;
			default : throw new RuntimeException("unsupported key type");
		}
	}
	
	private Integer getDataMemoryIndex() {
		return sramsAllocated;
	}
	
	private void updateDataMemoryIndex(Integer tableId, Integer rows, Integer cols, List<Memory> memories) {
		tableDataBoundariesMap.put(tableId, Pair.of(rows, cols)); 
		//updateSramAvailability(tableId, rows, cols);
		for(Memory memory: memories) {
			BitSet tid = new BitSet(somConfig.getMaxTables());
			tid.set(tableId);
			SRAMController sramController = new SRAMController(memory.getId(), somId, 1, 0, 0, tid, memory.getSegId(), 0, 0, 0, 1, 0, memory.getAddrRange().first(), memory.getAddrRange().second());
			sramControllers.add(sramController);
		}
		sramsAllocated += rows * cols;
	}

	private boolean isTherePlaceForData(Integer dataWidth, Integer tableId) {
		int numSramsRequiredForData = numSramsRequiredForData(dataWidth, tableId);
		boolean isTherePlaceForData = ((somConfig.getSramConfig().getNumSram() - sramsAllocated) >= numSramsRequiredForData) && ((somConfig.getSramConfig().getNumSram() - reservedSrams) >= numSramsRequiredForData);
		reservedSrams += numSramsRequiredForData;
		L.debug("total srams in som"+somId+" is "+somConfig.getSramConfig().getNumSram()+", total srams already allocated-"+sramsAllocated+", number of srams required for data-"+numSramsRequiredForData+", is there place for data for table-"+tableId+" : " + isTherePlaceForData);
		return isTherePlaceForData;
	}
	
	private Integer numSramsRequiredForData(Integer dataWidth, Integer tableId) {
		int wordSidePacking = (int) Math.ceil((double) (tableNumEntriesMap != null && tableNumEntriesMap.get(tableId) != null ? tableNumEntriesMap.get(tableId) : somConfig.getEntriesPerTable())/somConfig.getSramConfig().getWords());
		int bitSidePacking = (int) Math.ceil((double) dataWidth/somConfig.getSramConfig().getBits());
		return wordSidePacking * bitSidePacking;
	}
	
	private boolean isKeySegsAvailable(Integer keyWidth, Set<Integer> ksegIdsExcList, Integer disjointId) {
		int keySegsRequired = (int) Math.ceil((double) keyWidth/somConfig.getKsegWidth());
		
		int minfree = 0;
		for(int i=0; i<somConfig.getNumKseg(); i++) {
			if((ksegIdsExcList != null && !ksegIdsExcList.contains(i) && !ksegIdsAllocated.get(disjointId).contains(i))
					|| (ksegIdsExcList == null && !ksegIdsAllocated.get(disjointId).contains(i))) {
				minfree++;
			}
		}
		boolean isKeySegsAvailable = minfree >= keySegsRequired;
		L.debug("key segments required for key length-"+keyWidth+" is "+keySegsRequired+ ", free key segments-"+minfree+", isKeySegmentAvailble-"+isKeySegsAvailable);
		return isKeySegsAvailable;
	}
	
	private boolean assignKeySeg(Integer tableId, Integer keyWidth, Set<Integer> ksegIdsExcList, Integer disjointId) {
		if(isKeySegsAvailable(keyWidth, ksegIdsExcList, disjointId)) {
			int ksegsRequired = (int) Math.ceil((double) keyWidth/somConfig.getKsegWidth());
			Set<Integer> ksegsToBeAllocated = new HashSet<Integer>();
			for(int i=0; i<somConfig.getNumKseg(); i++) {
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
			L.debug("num key segs assigned to table-"+tableId+" : "+ksegsRequired);
			return true;
		}
		return false;
	}
	
	private Set<Integer> getAvailableDsegs(Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		int dataSegsRequired = (int) Math.ceil((double) dataWidth/somConfig.getDsegWidth());
		
		if(somConfig.getNumDseg() - dsegsAllocated.get(disjointId) >= dataSegsRequired) {
			
			for(int i=0; i<=(somConfig.getNumDseg()-dataSegsRequired); i++) {
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
					L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", isDataSegmentsAvailable-true");
					return availableDsegs;
				}
			}
			L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", but consecutive dsegs are not available");
			return null;
		}
		L.debug("num dsegs in som"+somId+" is "+somConfig.getNumDseg()+", dsegs already allocated-"+dsegsAllocated.get(disjointId)+", dsegs required-"+dataSegsRequired+", returning null");
		return null;
	}
	
	private boolean isDataSegsAvailable(Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		return getAvailableDsegs(dataWidth, dsegIdsExcList, disjointId) != null;
	}
	
	private boolean assignDataSeg(Integer tableId, Integer dataWidth, Set<Integer> dsegIdsExcList, Integer disjointId) {
		Set<Integer> dsegsToBeAllocated = getAvailableDsegs(dataWidth, dsegIdsExcList, disjointId);
		if(dsegsToBeAllocated != null) {
			for(Integer dsegId: dsegsToBeAllocated) 
				dsegIdsAllocated.get(disjointId).add(dsegId);
			tableDsegMap.put(tableId, dsegsToBeAllocated);
			dsegsAllocated.put(disjointId, dsegsAllocated.get(disjointId) + dsegsToBeAllocated.size());
			L.debug("num data segs assigned to table-"+tableId+" : "+dsegsToBeAllocated.size());
			return true;
		}
		return false;
	}
	
	private void addTableController(Integer tableId, ControllerType controller) {
		if(tableControllerMap.get(tableId) != null) {
			if(tableControllerMap.get(tableId).get(controller) == null)
				tableControllerMap.get(tableId).put(controller, 1);
			else
				tableControllerMap.get(tableId).put(controller, tableControllerMap.get(tableId).get(controller)+1);
		}
		else {
			tableControllerMap.put(tableId, new HashMap<ControllerType, Integer>());
			tableControllerMap.get(tableId).put(controller, 1);
		}
	}
	
	private boolean isCamControllerAvailable() {
		L.debug("iscamcontrollerAvailable-"+(somConfig.getNumCamControllers() != null && camControllersAllocated < somConfig.getNumCamControllers()));
		return somConfig.getNumCamControllers() != null && camControllersAllocated < somConfig.getNumCamControllers();
	}
	
	private boolean assignCamController(Integer tableId) {
		if(isCamControllerAvailable()) {
			addTableController(tableId, ControllerType.cam);
			camControllersAllocated++;
			return true;
		}
		return false;
	}
	
	public boolean hasTable(Integer tableId) {
		return tablesAllocated.containsKey(tableId);
	}
	
	private boolean isHashControllerAvailable() {
		L.debug("ishashcontrollerAvailable-"+(somConfig.getNumHashControllers() != null && hashControllersAllocated < somConfig.getNumHashControllers()));
		return somConfig.getNumHashControllers() != null && hashControllersAllocated < somConfig.getNumHashControllers();
	}
	
	private boolean assignHashController(Integer tableId) {
		if(isHashControllerAvailable()) {
			addTableController(tableId, ControllerType.hash);
			hashControllersAllocated++;
			return true;
		}
		return false;
	}
	
	private boolean assignReadController(Integer tableId) {
		if(isReadControllerAvailable()) {
			addTableController(tableId, ControllerType.read);
			readControllersAllocated++;
			return true;
		}
		return false;
	}
	
	private boolean isReadControllerAvailable() {
		L.debug("isreadcontrollerAvailable-"+(somConfig.getNumReadControllers() != null && readControllersAllocated < somConfig.getNumReadControllers()));
		return somConfig.getNumReadControllers() != null && readControllersAllocated < somConfig.getNumReadControllers();
	}
	
	private boolean isWriteControllerAvailable() {
		return writeControllersAllocated < somConfig.getNumWriteControllers();
	}
	
	public Set<Integer> getAllocatedTableIds() {
		return tablesAllocated.keySet(); 
	}
	
	public Set<Integer> getDsegsAllocatedForTable(Integer tableId) {
		return tableDsegMap.get(tableId);
	}
	
	public Set<Integer> getKsegsAllocatedForTable(Integer tableId) {
		return tableKsegMap.get(tableId);
	}
	
	public Map<ControllerType, Integer> getControllersAllocatedForTable(Integer tableId) {
		return tableControllerMap.get(tableId);
	}
	
	public Integer getNumSramsOfTable(Integer tableId) {
		int numSrams = 0;
		Table table = tablesAllocated.get(tableId);
		if(tableKeyTypeMap.get(tableId).equals(KeyType.exact))
			numSrams += table.getNumKeyMemories();
		numSrams += table.getNumDataMemories();
		return numSrams;
	}
	
	public Integer getNumTcamsOfTable(Integer tableId) {
		return !tableKeyTypeMap.get(tableId).equals(KeyType.exact) ? 2 * tablesAllocated.get(tableId).getNumKeyMemories() : 0;
	}
	
	private void updateCamControllers(Integer tableId) {
		BitSet readBitmap = new BitSet(somConfig.getNumDseg());
		for(Integer segId : tableDsegMap.get(tableId)) {
			readBitmap.set(segId);
		}
		CAMController camController = new CAMController(camControllers.size(), somId, 1, tableId, readBitmap, somConfig.getMissPtr(), tcamGrid.getSearchEnable(tableId));
		camControllers.add(camController);
	}
	
	private void updateReadControllers(Integer tableId) {
		BitSet readBitmap = new BitSet(somConfig.getNumDseg());
		for(Integer segId : tableDsegMap.get(tableId)) {
			readBitmap.set(segId);
		}
		ReadController readController = new ReadController(readControllers.size(), somId, 1, tableId, readBitmap);
		readControllers.add(readController);
	}
	
	private void updateHashControllers(Integer tableId) {
		if(hashTableList.contains(tableId)) {
			BitSet readBitmap = new BitSet(somConfig.getNumDseg());
			BitSet keyBitmap = new BitSet(somConfig.getNumKseg());
			BitSet mask = new BitSet(somConfig.getHashConfig().getCrcLength());
			mask.set(0, somConfig.getHashConfig().getMaskLength());
			for(Integer segId : tableDsegMap.get(tableId)) {
				readBitmap.set(segId);
			}
			for(Integer segId : tableKsegMap.get(tableId)) {
				keyBitmap.set(segId);
			}
			int indSeg = 0;
			for(int i=0; i<keyBitmap.length(); i++) {
				if(keyBitmap.get(i)) {
					indSeg = i;
					break;
				}
			}
			HashController hashController = new HashController(hashControllers.size(), somId, 1, tableId, readBitmap, indSeg, keyBitmap, mask, somConfig.getMissPtr());
			hashControllers.add(hashController);
		
			int maxAddr = (int) Math.pow(2,somConfig.getHashConfig().getMaskLength());
			List<Integer> segIdList = new ArrayList<Integer>(tableKsegMap.get(tableId));
			//int segId = 0;
			for(int i=0; i<maxAddr; i+=somConfig.getSramConfig().getWords()) {
				for(int j=0; j<somConfig.getHashConfig().getNumHway(); j++) {
					for(int k=0; k<segIdList.size(); k++) {
						BitSet tid = new BitSet(somConfig.getMaxTables());
						tid.set(tableId);
						SRAMController sramController = new SRAMController(tablesAllocated.get(tableId).getPhysicalMemoryId(i/somConfig.getSramConfig().getWords(), j, k, false), somId, 0, 0, 1, tid, 0, 0, segIdList.get(k), j, 0, 1, i, i+somConfig.getSramConfig().getWords()-1);
						sramControllers.add(sramController);
					}
				}
			}
		}
		else {
			BitSet readBitmap = new BitSet(somConfig.getNumDseg());
			for(Integer segId : tableDsegMap.get(tableId)) {
				readBitmap.set(segId);
			}
			ReadController readController = new ReadController(readControllers.size(), somId, 1, tableId, readBitmap);
			readControllers.add(readController);
		}
	}
	
	public void updateControllers() {
		for(Integer tableId : tableKeyTypeMap.keySet()) {
			switch(tableKeyTypeMap.get(tableId)) {
				case exact : updateHashControllers(tableId); break;
				case lpm : 
				case ternary : updateCamControllers(tableId); break;
				default : throw new RuntimeException("unsupported key type");
			}
		}
	}
	
	public LayoutType getLayoutType(Integer tableId) {
		return tablesAllocated.get(tableId).getLayoutType();
	}
	
	public List<List<Integer>> getKeyMemoryLayout(Integer tableId) {
		return tablesAllocated.get(tableId).getKeyMemoryLayout();
	}
	
	public List<List<Integer>> getDataMemoryLayout(Integer tableId) {
		return tablesAllocated.get(tableId).getDataMemoryLayout();
	}
	
	public String emitModelConfig() {
		StringBuilder sb = new StringBuilder();
		for(Integer tableId : tablesAllocated.keySet()) {
			sb.append(tablesAllocated.get(tableId).emitModelConfig(somId));
			sb.append("sos_loop["+ somId +"].somModel.cfg_tbl_sel["+tableId+"] = "+tableId + ";\n");
			BitSet bs = new BitSet(somConfig.getNumDseg());
			int index = 0;
			for(int i=tableDsegMap.get(tableId).iterator().next(); i<somConfig.getNumDseg(); i++) {
				if(tableDsegMap.get(tableId).contains(i))
					bs.set(index);
				index++;
			}
			sb.append("sos_loop["+ somId +"].somModel.cfg_dat_sel["+tableId+"] = "+ tableDsegMap.get(tableId).iterator().next() +";\n");
			sb.append("sos_loop["+ somId +"].somModel.cfg_dat_vld["+tableId+"] = "+ Utils.bitSetToLong(bs) +";\n");
			sb.append("sos_loop["+ somId +"].somModel.cfg_miss_ptr["+tableId+"] = 0;\n");
		}
		return sb.toString();
	}
	
	public void emitRTLConfig(File output) {
		if(camControllers != null) {
			for(CAMController camController : camControllers) {
				camController.emitRTLConfig(output);
			}
		}
		if(tcamGrid != null) {
			tcamGrid.emitRTLConfig(output);
		}
		if(sramControllers != null) {
			for(SRAMController sramController : sramControllers) {
				sramController.emitConfig(output);
			}
		}
		if(readControllers != null) {
			for(ReadController readController : readControllers) {
				readController.emitConfig(output);
			}
		}
		if(hashControllers != null) {
			for(HashController hashController : hashControllers) {
				hashController.emitConfig(output);
			}
		}
	}
	
	public Integer getNumEntriesInTable(Integer tableId) {
		if(tablesAllocated.get(tableId) != null)
			return tablesAllocated.get(tableId).getMaxEntries();
		throw new RuntimeException("som id " + somId + " does not contain table id " + tableId);
	}
	
	public void emitKeyAndData(File output) {
		File dir = new File(output.getAbsolutePath() + "/som_" + somId);
		File tcamFile = new File(dir.getAbsolutePath() + "/somTcamCfg.sv");
		try {
			dir.mkdirs();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		FileUtils.writeToFile(tcamFile, true, "initial begin\n@(posedge clk);\n@(negedge rst);\n@(posedge clk);\n");
		for(Integer tableId: tablesAllocated.keySet()) {
			tablesAllocated.get(tableId).emitKeyAndData(dir);
		}
		FileUtils.writeToFile(tcamFile, true, "end");
		emitPossibleTableAccesses(dir);
		emitTableKsegAndDesg(dir);
		for(Integer tableId : tablesAllocated.keySet())
			tablesAllocated.get(tableId).emitKeyAndDataInLogicalFormat(dir, somConfig.getNumKseg(), somConfig.getNumDseg());
	}
	
	private void emitPossibleTableAccesses(File dir) {
		try {
			File ksegFile = new File(dir.getAbsolutePath() + "/tableKseg.txt");
			File dsegFile = new File(dir.getAbsolutePath() + "/tableDseg.txt");
			FileUtils.writeToFile(ksegFile, true, "");
			FileUtils.writeToFile(dsegFile, true, "");
			int[] tableIds = new int[tablesAllocated.keySet().size()];
			int[] isSelected = new int[tableIds.length];
			int i=0;
			for(Integer tableId: tablesAllocated.keySet()) 
				tableIds[i++] = tableId;
			List<List<Integer>> allCombs = new ArrayList<List<Integer>>();
			for(i=1; i<=tableIds.length; i++)
				Utils.allCombinations(tableIds, i, 0, 0, tableIds.length, isSelected, allCombs);
			int tableBits = 8;
			int bitPerRow = somConfig.getNumKseg() * tableBits;
			for(List<Integer> comb : allCombs) {
				if(isCombPossible(comb)) {
					BitSet ksegBs = new BitSet(bitPerRow);
					BitSet dsegBs = new BitSet(bitPerRow);
					ksegBs.set(0, bitPerRow);
					dsegBs.set(0, bitPerRow);
					for(Integer tableId: comb) {
						//String tableHex = Utils.bitSetToHex(Utils.longToBitset(tableId, tableBits), tableBits);
						BitSet tableBS = Utils.longToBitset(tableId, tableBits);
						for(Integer segId : tableKsegMap.get(tableId)) {
							int index = segId * tableBits;
							//ksegAccesses[segId] = tableHex;
							for(int j=0; j<tableBits; j++) {
								if(tableBS.get(j))
									ksegBs.set(index + j);
								else
									ksegBs.clear(index + j);
							}
							index++;
						}
						for(Integer segId : tableDsegMap.get(tableId)) {
							int index = segId * tableBits;
							//ksegAccesses[segId] = tableHex;
							for(int j=0; j<tableBits; j++) {
								if(tableBS.get(j))
									dsegBs.set(index + j);
								else
									dsegBs.clear(index + j);
							}
							index++;
						}
					}
					//bs.set(index, bitPerRow);
					FileUtils.writeToFile(ksegFile, true, bitPerRow + "'h" + Utils.bitSetToHex(ksegBs) + "\n");
					FileUtils.writeToFile(dsegFile, true, bitPerRow + "'h" + Utils.bitSetToHex(dsegBs) + "\n");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void emitTableKsegAndDesg(File dir) {
		try {
			File file = new File(dir.getAbsolutePath() + "/tableKsegAndDseg.txt");
			int numBits = somConfig.getNumKseg() + somConfig.getNumDseg() + 1;
			//assuming max 32 tables
			for(int i=0; i<32; i++) {
				BitSet bs = new BitSet(somConfig.getNumKseg() + somConfig.getNumDseg() + 1);
				if((tableKsegMap.get(i) != null && tableDsegMap.get(i) == null)
						|| (tableKsegMap.get(i) == null && tableDsegMap.get(i) != null))
					throw new RuntimeException("Something went wrong. A table must have both kseg and dseg assigned");
				if(tableKsegMap.get(i) != null) {
					for(Integer segId : tableKsegMap.get(i))
						bs.set(somConfig.getNumDseg() + segId);
					for(Integer segId : tableDsegMap.get(i))
						bs.set(segId);
					bs.set(somConfig.getNumKseg() + somConfig.getNumDseg());
				}
				FileUtils.writeToFile(file, true, numBits + "'h" + Utils.bitSetToHex(bs) + "\n");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void emitDebugInfo(File output) {
		if(tcamGrid != null) {
			tcamGrid.emitDebugInfo(output);
		}
		if(sramControllers != null) {
			for(SRAMController sramController : sramControllers) {
				sramController.emitDebugInfo(output);
			}
		}
	}
	
	private boolean isCombPossible(List<Integer> comb) {
		int[] ksegs = new int[somConfig.getNumKseg()];
		int[] dsegs = new int[somConfig.getNumDseg()];
		for(Integer tableId : comb) {
			for(Integer segId : tableKsegMap.get(tableId)) {
				if(ksegs[segId] == 1)
					return false;
				else
					ksegs[segId] = 1;
			}
			for(Integer segId : tableDsegMap.get(tableId)) {
				if(dsegs[segId] == 1)
					return false;
				else
					dsegs[segId] = 1;
			}
		}
		return true;
	}
}
