package com.p4.pktgen.model.memory.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

public class CacheTable {

	private int tableId;
	private int baseAddr;
	private int entrySize;
	private int numLines;
	private int maxEntries;
	
	private int maxKeySize;
	private int maxDataSize;
	
	List<CacheEntry> tableEntries;
	CacheRegisterConfig cacheRegConfig;
	
	public CacheTable(int id, int addr, int lines, int numEntries, int keySize, int dataSize) {
		tableId = id;
		baseAddr = addr;
		numLines = lines;
		maxEntries = numEntries;
		
		maxKeySize = keySize;
		maxDataSize = dataSize;
		
		tableEntries = new ArrayList<CacheEntry>();
	}
	
	public boolean isNotFull() {
		return tableEntries.size() < maxEntries;
	}
	
	public void addEntry(BitSet key, Integer keyLength, BitSet actionData, Integer dataLength, BitSet mask, BitSet actionPtr) {
		tableEntries.add(new CacheEntry(key, actionData, actionPtr));
	}
	
	public void setConfig(Set<Integer> ksegs, Set<Integer> dsegs) {
		
		int entryValidPos = 0;
		int entryValidSize = 1;
		int keyPos = entryValidPos + entryValidSize;
		int keySize = maxKeySize/8;
		int datPos = keyPos + keySize;
		int datSize = maxDataSize/8;
		int ptrPos = datPos + datSize;
		int ptrSize = 1; //hard coded for now
		int rmwPtr = ptrPos + ptrSize;
		int addrMode = 0; //direct addressing
		int missPtr = 0; //hard coded
		int readOnly = 1; //hard coded
		int defPtr = 0; //hard coded
		
		BitSet ksegMap = new BitSet();
		BitSet dsegMap = new BitSet();
		//BitSet qsegMap = new BitSet();
		//BitSet psegMap = new BitSet();
		
		for(Integer id : ksegs)
			ksegMap.set(id);
		
		for(Integer id : dsegs)
			dsegMap.set(id);
		
		cacheRegConfig = new CacheRegisterConfig();
		cacheRegConfig.setAddrMode(addrMode);
		cacheRegConfig.setBaseAddr(baseAddr);
		cacheRegConfig.setDatPos(datPos);
		cacheRegConfig.setDatSize(datSize);
		cacheRegConfig.setDefaultPtr(defPtr);
		cacheRegConfig.setDefOnMiss(missPtr);
		cacheRegConfig.setDsegMap(dsegMap);
		cacheRegConfig.setEnableCfg(1);
		cacheRegConfig.setEntrySize(numLines-1);
		cacheRegConfig.setKeyMap(ksegMap);
		cacheRegConfig.setKeyPos(keyPos);
		cacheRegConfig.setKeySize(keySize);
		cacheRegConfig.setNumEntry(maxEntries);
		cacheRegConfig.setPsegMap(new BitSet());
		cacheRegConfig.setPtrPos(ptrPos);
		cacheRegConfig.setPtrSize(ptrSize);
		cacheRegConfig.setQidSeg(0); //hard coded
		cacheRegConfig.setReadOnly(1);
		cacheRegConfig.setRmwPtr(0);
		cacheRegConfig.setTableId(tableId);
		cacheRegConfig.setValidPos(entryValidPos);
	}
	
	public void emit(File configData, File tableData) {
		emitConfig(configData);
		emitData(tableData);
	}
	
	private void emitConfig(File configData) {
		
		FileUtils.writeToFile(configData, true, tableId+" entry_vld_pos " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getValidPos())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" read_only " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getReadOnly())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" key_pos " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getKeyPos())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" key_siz " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getKeySize())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" dat_pos " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getDatPos())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" dat_siz " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getDatSize())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" ptr_pos " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getPtrPos())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" ptr_siz " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getPtrSize())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" rmwptr_pos " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getRmwPtr())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" base " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getBaseAddr())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" id " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getTableId())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" key_map " + Utils.bitSetToHex(cacheRegConfig.getKeyMap()) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" qid_seg " + Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getQidSeg())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" dseg_map " + Utils.bitSetToHex(cacheRegConfig.getDsegMap()) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" pseg_map " + Utils.bitSetToHex(cacheRegConfig.getPsegMap()) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" adr_mode " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getAddrMode())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" enable_cfg " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getEnableCfg())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" num_entr " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getNumEntry())) + "\n");
		FileUtils.writeToFile(configData, true, tableId+" entrysize " +  Utils.bitSetToHex(Utils.longToBitset(cacheRegConfig.getEntrySize())) + "\n");
	}
	
	private void emitData(File tableData) {
		int index = baseAddr;
		for(CacheEntry entry : tableEntries) {
			FileUtils.writeToFile(tableData, true, tableId+" addr " + Utils.bitSetToHex(Utils.longToBitset(Utils.bitSetToLong(entry.getKey()) * numLines * 32)) + "\n");
			FileUtils.writeToFile(tableData, true, tableId+" key " + Utils.bitSetToHex(entry.getKey()) + "\n");
			FileUtils.writeToFile(tableData, true, tableId+" data " + Utils.bitSetToHex(entry.getData()) + "\n");
			FileUtils.writeToFile(tableData, true, tableId+" ptr " + Utils.bitSetToHex(entry.getPtr()) + "\n");
			FileUtils.writeToFile(tableData, true, tableId+" rmwptr 0\n");
			index += (numLines * 32);
		}
	}
}
