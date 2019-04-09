package com.p4.pktgen.cuckoo;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import lombok.Getter;

import com.p4.pktgen.model.memory.Memory;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class Cuckoo {

	private int numHway;
	private int tableDepth;
	private int unitDepth;
	private List<List<BitSet>> tablesList;
	private int crcLen;
	private int maskLen;
	private int numSegs;
	private BitSet[][][] keys;
	private Map<BitSet, Integer[]> keyLocations;
	@Getter private Memory[][] tables;
	
	
	public Cuckoo(int numhway, int depth, int cLen, int mLen) {
		numHway = numhway;
		tableDepth = depth;
		tablesList = new ArrayList<List<BitSet>>(tableDepth);
		crcLen = cLen;
		maskLen = mLen;
		
		for(int i=0; i<tableDepth; i++) {
			tablesList.add(new ArrayList<BitSet>(numHway));
			for(int j=0; j<numHway; j++)
				tablesList.get(i).add(null);
		}
	}
	
	public Cuckoo(Memory[][] memories, int numhway, int depth, int cLen, int mLen) {
		tables = memories;
		numHway = numhway;
		unitDepth = depth;
		numSegs = memories[0].length/numhway;
		//crcLen = Utils.lg(memories.length * unitDepth);
		crcLen = cLen;
		maskLen = mLen;
		reset();
	}
	
	public void reset() {
		keys = new BitSet[tables.length][numHway * tables[0].length][tables[0][0].getDepth()];
		keyLocations = new HashMap<BitSet, Integer[]>();
		for(int i=0; i<tables.length; i++) {
			for(int j=0; j<tables[i].length; j++) {
				for(int k=0; k<tables[i][j].getDepth(); k++) {
					tables[i][j].addEntry(k, null);
					keys[i][j][k] = null;
				}
			}
		}
	}
	
	public List<List<Integer>> getKeyMemoryLayout() {
		List<List<Integer>> memLayout = new ArrayList<List<Integer>>();
		for(int i=0; i<tables.length; i++) {
			List<Integer> waysLayout = new ArrayList<Integer>();
			for(int j=0; j<tables[i].length; j++) {
				waysLayout.add(tables[i][j].getId());
			}
			memLayout.add(waysLayout);
		}
		return memLayout;
	}
	
	public boolean insertEntry(BitSet key, int instOffset, int instLen, int numSegs, int wayId, int keySize, int totalSize, Queue<Pair<BitSet,Integer>> cuckooedEntries, int numTries) {
		//List<Integer> keySegAddrLocations = new ArrayList<Integer>();
		
		if(numTries == 30)
			return false;
		
		int keyOffset = 0;
		int nextWayId = (wayId + 1) == numHway ? 0 : wayId + 1;
		for(int seg=0; seg<numSegs; seg++) {
			BitSet partKey = key.get(keyOffset, keyOffset + keySize);
			BitSet crc = CRC.getCRCResult(partKey, wayId, crcLen);
			BitSet mask = new BitSet(crcLen);
			mask.set(0, maskLen);
			crc.and(mask);
			Integer index = (int) Utils.bitSetToLong(crc);
			
			if(tables[index/unitDepth][wayId * numSegs + seg].getEntry(index%unitDepth) != null) {
				
				BitSet poppedOutEntry = keys[index/unitDepth][wayId * numSegs + seg][index%unitDepth];
				cuckooedEntries.add(Pair.of(poppedOutEntry, nextWayId));
				for(int s=0; s<numSegs; s++) {
					int poppedIndex = keyLocations.get(poppedOutEntry)[wayId * numSegs + s];
					tables[poppedIndex/unitDepth][wayId * numSegs + s].addEntry(poppedIndex%unitDepth, null);
					keys[poppedIndex/unitDepth][wayId * numSegs + s][poppedIndex%unitDepth] = null;
				}
				keyLocations.remove(poppedOutEntry);
			}
			BitSet hashEntry = new BitSet(totalSize);
			int entryIndex = 0;
			for(int i=0; i<keySize; i++) {
				if(partKey.get(i))
					hashEntry.set(entryIndex);
				entryIndex++;
			}
			for(int i=instOffset; i<instOffset+instLen; i++) {
				if(key.get(i))
					hashEntry.set(entryIndex);
				entryIndex++;
			}
			hashEntry.set(totalSize-1);
			tables[index/unitDepth][wayId * numSegs + seg].addEntry(index%unitDepth, hashEntry);
			keys[index/unitDepth][wayId * numSegs + seg][index%unitDepth] = key;
			
			if(keyLocations.get(key) == null)
				keyLocations.put(key, new Integer[numHway * numSegs]);
			
			keyLocations.get(key)[wayId * numSegs + seg] = index;
			
			keyOffset += keySize;
		}
		
		if(!cuckooedEntries.isEmpty()) {
			Pair<BitSet, Integer> entryToBeReinserted = cuckooedEntries.remove();
			return insertEntry(entryToBeReinserted.first(), instOffset, instLen, numSegs, entryToBeReinserted.second(), keySize, totalSize, cuckooedEntries, numTries+1);
		}
		else {
			return true;
		}
	}
	
	public void insertEntry(BitSet entry, int segId, int wayId, int addrOffset, int addrSize) {
		int currTable = wayId;
		BitSet currentEntry = entry;
		int numTries = 0;
		while(true) {
			BitSet crc = CRC.getCRCResult(currentEntry.get(addrOffset, addrOffset + addrSize), currTable, crcLen);
			BitSet mask = new BitSet(crcLen);
			mask.set(0, maskLen);
			crc.and(mask);
			Integer index = (int) Utils.bitSetToLong(crc);
			if(tables[index/unitDepth][currTable * numSegs + segId].getEntry(index%unitDepth) == null) {
				tables[index/unitDepth][currTable * numSegs + segId].addEntry(index%unitDepth, currentEntry);
				break;
			}
			else {
				numTries++;
				if(numTries == 30)
					throw new RuntimeException("Unable to cuckoo the entry within 30 tries");
				BitSet evictedEntry = tables[index/unitDepth][currTable * numSegs + segId].getEntry(index%unitDepth);
				tables[index/unitDepth][currTable * numSegs + segId].addEntry(index%unitDepth, currentEntry);
				currentEntry = evictedEntry;
				currTable++;
				if(currTable == numHway)
					currTable = 0;
			}
		}
	}
	
	public void insertEntry(BitSet entry, int wayId, int addrOffset, int addrSize) {
		int currTable = wayId;
		BitSet currentEntry = entry;
		int numTries = 0;
		while(true) {
			BitSet crc = CRC.getCRCResult(currentEntry.get(addrOffset, addrOffset + addrSize), currTable, crcLen);
			BitSet mask = new BitSet(crcLen);
			mask.set(0, maskLen);
			crc.and(mask);
			//System.out.println("after mask : " + Utils.bitSetToHex(crc));
			Integer index = (int) Utils.bitSetToLong(crc);
			if(tablesList.get(index).get(currTable) == null) {
				tablesList.get(index).set(currTable, currentEntry);
				break;
			}
			else {
				numTries++;
				if(numTries == 100) {
					int occupied = 0;
					int total = 0;
					for(int i=0; i<tableDepth; i++) {
						for(int j=0; j<numHway; j++) {
							total++;
							if(tablesList.get(i).get(j) != null) 
								occupied++;
						}
					}
					System.out.println("% occupied : " + occupied*100/total);
					throw new RuntimeException("Unable to cuckoo the entry within 30 tries");
				}
				BitSet evictedEntry = tablesList.get(index).get(currTable);
				tablesList.get(index).set(currTable, currentEntry);
				currentEntry = evictedEntry;
				currTable++;
				if(currTable == numHway)
					currTable = 0;
			}
		}
	}
	
	public void emitKeyAndData(File outputDir) {
		for(int i=0; i<tables.length; i++) {
			for(int j=0; j<tables[i].length; j++) {
				tables[i][j].emitKeyAndData(outputDir);
			}
		}
	}
	
	public int getPhysicalMemoryId(int row, int col, int segId) {
		return tables[row][col * numSegs + segId].getId();
	}
	
	public static void main(String[] args) {
		Cuckoo cuc = new Cuckoo(2, (int) Math.pow(2,11), 15, 11);
		for(int i=0; ; i++) {
			System.out.println(i);
			cuc.insertEntry(Utils.randomBitSet(80), 0, 0, 80);
		}
	}
}
