package com.p4.pktgen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Getter;

import com.p4.pktgen.p4blocks.ActionParams;
import com.p4.pktgen.p4blocks.ControlBlock;
import com.p4.pktgen.p4blocks.TableBlock;
import com.p4.pktgen.p4blocks.TableKey;
import com.p4.utils.Utils;

@Getter
public class TableSpace {
	
	private P4Blocks p4blocks;
	private Map<Integer,Map<String, Integer>> actionsInstMap;
	private Integer instructionPtrLen;
	
	private Map<Integer, List<TableEntry>> randomTableEntries;
	private Map<Integer, List<TableEntry>> fixedTableEntries;
	private Integer fixedTableEntriesSize;
	
	private final int maxEntries = 1023;

	public TableSpace(P4Blocks blocks, Map<Integer, Map<String, Integer>> instMap, Integer ptrLen) {
		p4blocks = blocks;
		actionsInstMap = instMap;
		instructionPtrLen = ptrLen;
		
		randomTableEntries = new HashMap<Integer, List<TableEntry>>();
		fixedTableEntries = new HashMap<Integer, List<TableEntry>>();
		fixedTableEntriesSize = 0;
		
		init();
	}
	
	public void init() {
		for(Map.Entry<String, ControlBlock> controlEntry : p4blocks.getControlBlocksExtracted().entrySet()) {
			for(Map.Entry<String, TableBlock> tableEntry : controlEntry.getValue().getTablesExtracted().entrySet()) {
				
				if(actionsInstMap.get(tableEntry.getValue().getTableId()) == null) {
					continue;
				}
				
				int actionIdx = 0;
				for(int i=0; i<maxEntries; i++) {
					Map<String,BitSet> tableEntryKeys = new HashMap<String,BitSet>();
					for(TableKey keys : tableEntry.getValue().getTableKeys()) {
						tableEntryKeys.put(keys.getKeyName(), Utils.randomBitSet(keys.getSize()));
					}
					
					BitSet mask = new BitSet(tableEntry.getValue().getTotalWidthOfKeys());
					mask.set(0, tableEntry.getValue().getTotalWidthOfKeys());
					
					String action = tableEntry.getValue().getTableActions().get(actionIdx);
					Map<String,BitSet> actionsParams = new HashMap<String,BitSet>();
					for(ActionParams params : controlEntry.getValue().getActionsExtracted().get(action).getActionParams()) {
						actionsParams.put(params.getParamName(), Utils.randomBitSet(params.getParamWidth()));
					}
					actionIdx++;
					
					BitSet ptr = Utils.longToBitset(actionsInstMap.get(tableEntry.getValue().getTableId()).get(action).longValue(), instructionPtrLen);
					
					if(actionIdx == tableEntry.getValue().getTableActions().size())
						actionIdx = 0;
					
					TableEntry entry = new TableEntry(tableEntry.getValue().getTableName(), action, tableEntryKeys, mask, actionsParams, ptr);
					if(randomTableEntries.get(tableEntry.getValue().getTableId()) == null)
						randomTableEntries.put(tableEntry.getValue().getTableId(), new ArrayList<TableEntry>());
					randomTableEntries.get(tableEntry.getValue().getTableId()).add(entry);
				}
				
				List<TableEntry> fixedEntries = new ArrayList<TableEntry>();
				for(int i=0; i<maxEntries; i++) {
					fixedEntries.add(i, null);
				}
				fixedTableEntries.put(tableEntry.getValue().getTableId(), fixedEntries);
			}
		}
		
		
	}
	
	public boolean isFixedEntriesFull(int tableId) {
		return fixedTableEntries.get(tableId).size() >= maxEntries/2;
	}
	
	public Integer getNumFixedEntries(int tableId) {
		return fixedTableEntries.get(tableId) != null ? fixedTableEntries.get(tableId).size() : 0;
	}
	
	public void addKeyToFixedEntries(int tableId, int index, String key, BitSet value) {
		if(fixedTableEntries.get(tableId).get(index) == null) {
			fixedTableEntries.get(tableId).set(index, randomTableEntries.get(tableId).get(index));
			fixedTableEntriesSize++;
		}
		fixedTableEntries.get(tableId).get(index).getKeys().put(key, value);
	}
	
	public boolean doesFixedEntryAlreadyExist(int tableId, Map<String,BitSet> keys) {
		for(TableEntry entry : fixedTableEntries.get(tableId)) {
			int keyMatch = 0;
			int nullMatch = 0;
			for(String key: keys.keySet()) {
				if(keys.get(key) != null) {
					if(keys.get(key).equals(entry.getKeys().get(key)))
						keyMatch++;
				}
				else {
					nullMatch++;
				}
			}
			if((nullMatch + keyMatch) == keys.size())
				return true;
		}
		return false;
	}
	
	public Map<String,BitSet> getFixedTableEntryKeys(int tableId, int index) {
		return fixedTableEntries.get(tableId).get(index).getKeys();
	}
	
	public Map<String,BitSet> getRandomTableEntryKeys(int tableId) {
		int randomIdx = (new Random()).nextInt(maxEntries - 1 - ((maxEntries+1)/2) + 1) + ((maxEntries+1)/2);
		return randomTableEntries.get(tableId).get(randomIdx).getKeys();
	}
	
	public TableEntry getFixedTableEntry(int tableId, int index) {
		return fixedTableEntries.get(tableId).get(index);
	}
	
	public TableEntry getRandomTableEntry(int tableId) {
		int randomIdx = (new Random()).nextInt(maxEntries - 1 - ((maxEntries+1)/2) + 1) + ((maxEntries+1)/2);
		return randomTableEntries.get(tableId).get(randomIdx);
	}
	
	public List<TableEntry> getTableEntries(int tableId) {
		List<TableEntry> entries = new ArrayList<TableEntry>();
		for(int i=0; i<fixedTableEntriesSize; i++)
			entries.add(fixedTableEntries.get(tableId).get(i));
		for(int i=fixedTableEntriesSize; i<maxEntries; i++)
			entries.add(randomTableEntries.get(tableId).get(i));
		return entries;
	}
}
