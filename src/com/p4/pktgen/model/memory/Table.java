package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import lombok.Getter;

import com.p4.pktgen.config.som.SOMConfig;
import com.p4.pktgen.cuckoo.Cuckoo;
import com.p4.pktgen.enums.LayoutType;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class Table {

	@Getter private Integer id;
	@Getter private Integer maxEntries;
	@Getter private Integer kx;
	private Integer ky;
	@Getter private Integer dx;
	private Integer dy;
	private Memory[][] keys;
	//private Memory[][] masks;
	private Memory[][] data;
	private List<BitSet> actionPtrs;
	private Integer numKeyEntries;
	private Integer numDataEntries;
	private Integer keyMemoryIndex;
	private Integer dataMemoryIndex;
	private boolean isHashKey;
	private boolean isDirectAddressing;
	private Cuckoo cuckoo;
	private SOMConfig somConfig;
	private List<BitSet> hashKeys;
	private List<BitSet> logicalKeys;
	private List<BitSet> logicalData;
	private BitSet missPtrData;
	private Set<BitSet> nonrepeatedKeys;
	
	public Table(SOMConfig config, Integer xForKey, Integer yForKey, Integer xForData, Integer yForData, boolean isHash, boolean isDirectAddr) {
		kx = xForKey;
		ky = yForKey;
		dx = xForData;
		dy = yForData;
		keys = new Memory[ky][kx];
		data = new Memory[dy][dx];
		isHashKey = isHash;
		isDirectAddressing = isDirectAddr;
		somConfig = config;
		nonrepeatedKeys = new HashSet<BitSet>();
		
		numKeyEntries = 0;
		numDataEntries = 0;
		keyMemoryIndex = 0;
		dataMemoryIndex = 0;
	}
	
	public Table(SOMConfig config, Integer tableId, Integer keyIndex, Integer dataIndex, Pair<Pair<Integer,Integer>,List<Memory>> k, Pair<Pair<Integer,Integer>,List<Memory>> d, Integer limit, boolean isHash, boolean isDirectAddr) {
		id = tableId;
		kx = k != null ? k.first().second() : 0;
		ky = k != null ? k.first().first() : 0;
		dx = d.first().second();
		dy = d.first().first();
		keys = new Memory[ky][kx];
		data = new Memory[dy][dx];
		actionPtrs = new ArrayList<BitSet>();
		isHashKey = isHash;
		isDirectAddressing = isDirectAddr;
		somConfig = config;
		
		maxEntries = limit;
		numKeyEntries = 0;
		numDataEntries = 0;
		keyMemoryIndex = 0;
		dataMemoryIndex = 0;
		
		logicalKeys = new ArrayList<BitSet>();
		logicalData = new ArrayList<BitSet>();
		hashKeys = new ArrayList<BitSet>();
		
		if(k != null)
			assignMemories(tableId, keyIndex, keys, k.second(), ky, kx);
		assignMemories(tableId, dataIndex, data, d.second(), dy, dx);
		for(int i=0; i<dy; i++) {
			for(int j=0; j<data[i][0].getDepth(); j++) {
				actionPtrs.add(null);
			}
		}
		
		if(isHashKey) {
			cuckoo = new Cuckoo(keys, somConfig.getHashConfig().getNumHway(), keys[0][0].getDepth(), somConfig.getHashConfig().getCrcLength(), somConfig.getHashConfig().getMaskLength());
		}
	}
	
	private void assignMemories(Integer tableId, Integer startIndex, Memory[][] memArray, List<Memory> memList, Integer depth, Integer width) {
		int memIndex = 0;
		int startAddr = 0;
		int startMemIdx = startIndex;
		for(int i=0; i<depth; i++) {
			for(int j=0; j<width; j++) {
				memArray[i][j] = memList.get(memIndex++);
				memArray[i][j].setAddrRange(startAddr, startAddr + memArray[i][j].getDepth() - 1);
				memArray[i][j].setTableId(tableId);
				memArray[i][j].setId(startMemIdx++);
			}
			startAddr += memArray[i][0].getDepth();
		}
	}
	
	public boolean isNotFull() {
		return numKeyEntries < maxEntries && numDataEntries < maxEntries;
	}
	
	private void addLogicalKey(BitSet key) {
		BitSet logicalKey = new BitSet(somConfig.getNumKseg() * somConfig.getKsegWidth());
		
		for(int i=0; i<key.length(); i++)
			if(key.get(i))
				logicalKey.set(i);
		
		if(isDirectAddressing) {
			for(int i=0; i<logicalKeys.size(); i++) {
				if(logicalKeys.get(i).equals(logicalKey)) {
					logicalKeys.remove(i);
					logicalData.remove(i);
				}
			}
		}
		
		logicalKeys.add(logicalKey);
	}
	
	private void addLogicalData(BitSet actionData, BitSet actionPtr, boolean isMissPtr) {
		int size = somConfig.getNumDseg() * somConfig.getDsegWidth() + somConfig.getSramConfig().getBits() - somConfig.getDsegWidth();
		BitSet data = new BitSet(size);
		for(int i=0; i<actionData.length(); i++)
			if(actionData.get(i))
				data.set(i);
		int instPtrBits = somConfig.getInstPtrNumBits() != null ? somConfig.getInstPtrNumBits() : somConfig.getSramConfig().getBits() - somConfig.getDsegWidth() - 1;
		for(int i=0,j=somConfig.getNumDseg() * somConfig.getDsegWidth(); i<instPtrBits; i++,j++)
			if(actionPtr.get(i))
				data.set(j);
		data.set(size-1);
		if(isMissPtr)
			missPtrData = data;
		else
			logicalData.add(data);
	}
	
	private int addKey(BitSet key, BitSet mask, Integer kIndex) {
		int keySegWidth = somConfig.getKsegWidth();
		int keyUnitDepth = keys[0][0].getDepth();
		int keyEntryIndex = kIndex == null ? numKeyEntries : kIndex;
		int keyMemIndex = keyEntryIndex/keyUnitDepth;

		int index = 0;
		for(int i=0; i<kx; i++) {
			keys[keyMemIndex][i].addEntry(keyEntryIndex % keyUnitDepth, key.get(index, index+keySegWidth));
			index += keySegWidth;
		}
		numKeyEntries++;
		return keyEntryIndex;
	}
	
	private int addData(BitSet actionData, BitSet actionPtr, Integer dIndex) {
		
		int dataSegWidth = somConfig.getDsegWidth();
		int dataUnitDepth = data[0][0].getDepth();
		int dataEntryIndex = dIndex == null ? numDataEntries : dIndex;
		int dataMemIndex = dataEntryIndex/dataUnitDepth;
		
		int index = 0;
		for(int i=0; i<dx; i++) {
			BitSet sramEntry = new BitSet(data[dataMemIndex][i].getWidth());
			BitSet dataBs = actionData.get(index, index+dataSegWidth);
			int j=0;
			for(; j<data[dataMemIndex][i].getDataWidth(); j++)
				if(dataBs.get(j))
					sramEntry.set(j);
			int instPtrBits = somConfig.getInstPtrNumBits() != null ? somConfig.getInstPtrNumBits() : somConfig.getSramConfig().getBits() - somConfig.getDsegWidth() - 1;
			for(int k=0; j<(data[dataMemIndex][i].getDataWidth()+instPtrBits); j++,k++)
				if(actionPtr.get(k))
					sramEntry.set(j);
			sramEntry.set(data[dataMemIndex][i].getWidth()-1);
			data[dataMemIndex][i].addEntry(dataEntryIndex % dataUnitDepth, sramEntry);
			index += dataSegWidth;
		}
		
		actionPtrs.add(dataEntryIndex, actionPtr);
		numDataEntries++;
		return dataEntryIndex;
	}
	
	public void addEntry(BitSet key, Integer keyLength, BitSet actionData, Integer dataLength, BitSet mask, BitSet actionPtr) {
		
		if(numDataEntries == somConfig.getMissPtr()) {
			addData(Utils.stringToBitSet("0x000000000000000000DEADBF", dataLength > somConfig.getDsegWidth() ? dataLength : somConfig.getDsegWidth(), 16, true), 
					Utils.stringToBitSet("0x3AB", 10, 16, true), null);
			addLogicalData(Utils.stringToBitSet("0x000000000000000000DEADBF", dataLength > somConfig.getDsegWidth() ? dataLength : somConfig.getDsegWidth(), 16, true),
					Utils.stringToBitSet("0x3AB", 10, 16, true), true);
			//numDataEntries++;
		}
		
		if(isHashKey) {
			
			/*int dataIndex = addData(actionData, actionPtr, null);
			//BitSet entry = new BitSet(somConfig.getSramConfig().getBits());
			BitSet addr = Utils.longToBitset(dataIndex, somConfig.getInstPtrNumBits() != null ? somConfig.getInstPtrNumBits() : somConfig.getSramConfig().getBits() - somConfig.getKsegWidth() - 1);
			int numKeySegs = (int) Math.ceil((double)keyLength/somConfig.getKsegWidth());
			for(int seg=0; seg<numKeySegs; seg++) {
				BitSet entry = new BitSet(somConfig.getSramConfig().getBits());
				int index = 0;
				for(; index<somConfig.getKsegWidth(); index++) 
					if(key.get(seg*somConfig.getKsegWidth() + index))
						entry.set(index);
				for(int i=0; index<(somConfig.getSramConfig().getBits() - 1); i++,index++)
					if(addr.get(i))
						entry.set(index);
				entry.set(somConfig.getSramConfig().getBits() - 1);
				//index++;
				cuckoo.insertEntry(entry, seg, 0, 0, somConfig.getKsegWidth());
			}*/
			
			int dataIndex = addData(actionData, actionPtr, null);
			int instOffset = somConfig.getNumKseg() * somConfig.getKsegWidth();
			int instLen = somConfig.getInstPtrNumBits() != null ? somConfig.getInstPtrNumBits() : somConfig.getSramConfig().getBits() - somConfig.getKsegWidth() - 1;
			BitSet instAddr = Utils.longToBitset(dataIndex, instLen);
			int numKeySegs = (int) Math.ceil((double)keyLength/somConfig.getKsegWidth());
			BitSet logicalKey = new BitSet(somConfig.getNumKseg() * somConfig.getKsegWidth() + instLen);
			for(int i=0; i<keyLength; i++) {
				if(key.get(i))
					logicalKey.set(i);
			}
			for(int i=0,j=instOffset; i<instLen; i++,j++) {
				if(instAddr.get(i))
					logicalKey.set(j);
			}
			hashKeys.add(logicalKey);
			Queue<Pair<BitSet,Integer>> cuckooedEntries = new LinkedList<Pair<BitSet,Integer>>();
			boolean isKeyInserted = cuckoo.insertEntry(logicalKey, instOffset, instLen, numKeySegs, 0, somConfig.getKsegWidth(), somConfig.getSramConfig().getBits(), cuckooedEntries, 0);
			if(!isKeyInserted) {
				
				int numTries = 0;
				int retryLimit = 3;
				while (numTries < retryLimit) {
					cuckoo.reset();
					cuckooedEntries = new LinkedList<Pair<BitSet,Integer>>();
					Integer[] randomOrder = Utils.getRandomArray(hashKeys.size());
					for(int i=0; i<randomOrder.length; i++) {
						isKeyInserted = cuckoo.insertEntry(hashKeys.get(randomOrder[i]), instOffset, instLen, numKeySegs, 0, somConfig.getKsegWidth(), somConfig.getSramConfig().getBits(), cuckooedEntries, 0);
						if(!isKeyInserted) break;
					}
					numTries++;
				}
				if(numTries == retryLimit)
					throw new RuntimeException("Unable to insert keys into hash after multiple attempts");
			}
		}
		else {
			if(isDirectAddressing) {
				addData(actionData, actionPtr, (int) Utils.bitSetToLong(key));
			}
			else {
				addData(actionData, actionPtr, null);
				if(numKeyEntries == somConfig.getMissPtr()) {
					addKey(new BitSet(keyLength), new BitSet(keyLength), null);
					//numKeyEntries++;
				}
				addKey(key, mask, null);
			}
		}
		//addLogicalData(actionData, actionPtr);
		if(isDirectAddressing) {
			BitSet logicalKey = new BitSet();
			int bitAddr = Utils.lg(somConfig.getSramConfig().getNumSram() * somConfig.getSramConfig().getWords());
			for(int i=somConfig.getKsegWidth()-bitAddr,j=0; j<key.length(); i++,j++) {
				if(key.get(j))
					logicalKey.set(i);
			}
			addLogicalKey(logicalKey);
			addLogicalData(actionData, actionPtr, false);
		}
		else {
			addLogicalKey(key);
			addLogicalData(actionData, actionPtr, false);
		}
	}
	
	public LayoutType getLayoutType() {
		if(isDirectAddressing)
			return LayoutType.simple;
		else if(isHashKey)
			return LayoutType.hash;
		else
			return LayoutType.tcam;
	}
	
	public List<List<Integer>> getKeyMemoryLayout() {
		if(isDirectAddressing)
			return null;
		else if(isHashKey) 
			return cuckoo.getKeyMemoryLayout();
		else {
			List<List<Integer>> memLayout = new ArrayList<List<Integer>>();
			for(int i=0; i<keys.length; i++) {
				List<Integer> colsLayout = new ArrayList<Integer>();
				for(int j=0; j<keys[i].length; j++) {
					if(keys[i][j] instanceof TCAM) {
						colsLayout.add(((TCAM) keys[i][j]).getRowId());
						colsLayout.add(((TCAM) keys[i][j]).getColId());
					}
					else
						colsLayout.add(keys[i][j].getId());
				}
				memLayout.add(colsLayout);
			}
			return memLayout;
		}
	}
	
	public List<List<Integer>> getDataMemoryLayout() {
		List<List<Integer>> memLayout = new ArrayList<List<Integer>>();
		for(int i=0; i<data.length; i++) {
			List<Integer> colsLayout = new ArrayList<Integer>();
			for(int j=0; j<data[i].length; j++) {
				colsLayout.add(data[i][j].getId());
			}
			memLayout.add(colsLayout);
		}
		return memLayout;
	}
	
	public String emitModelConfig(Integer somId) {
		StringBuilder sb = new StringBuilder();
		
		int keyIndex = 0;
		int dataIndex = 0;
		
		int entryIndex = 0;
		for(int i=0; i<numKeyEntries; i++) {
			int j=kx-1,k=0; 
			for(j=0; j<kx; j++,k++) {
				BitSet mask = new BitSet(somConfig.getSramConfig().getBits());
				mask.set(keys[keyIndex][j].getEntry(entryIndex).length(), keys[keyIndex][j].getWidth());
				sb.append("sos_loop["+somId+"].somModel.tcam_data["+id+"]["+entryIndex+"]["+k+"]="+keys[keyIndex][j].emitModelConfig(entryIndex) + ";\n");
				sb.append("sos_loop["+somId+"].somModel.tcam_mask["+id+"]["+entryIndex+"]["+k+"]="+ keys[keyIndex][j].getWidth() + "'h" + Utils.bitSetToHex(mask) + ";\n");
			}
			entryIndex++;
			if(entryIndex % keys[0][0].getDepth() == 0) keyIndex++;
		}
		
		entryIndex = 0;
		for(int i=0; i<numDataEntries; i++) {
			int j=dx-1,k=0; 
			for(j=0; j<dx; j++,k++) {
				sb.append("sos_loop["+somId+"].somModel.sram_dat["+id+"]["+entryIndex+"]["+k+"]="+data[dataIndex][j].emitModelConfig(entryIndex) + ";\n");
			}
			sb.append("sos_loop["+somId+"].somModel.sram_ptr["+id+"]["+entryIndex+"]="+Utils.bitSetToLong(actionPtrs.get(entryIndex)) + ";\n");
			entryIndex++;
			if(entryIndex % data[0][0].getDepth() == 0) dataIndex++;
		}
		return sb.toString();
	}
	
	public void emitKeyAndDataInLogicalFormat(File dir, Integer numKsegs, Integer numDsegs) {
		
		try {
			File ifile = new File(dir.getAbsolutePath() + "/table_"+ id +"_logical_input.txt");
			File ofile = new File(dir.getAbsolutePath() + "/table_"+ id +"_logical_output.txt");
			File mfile = new File(dir.getAbsolutePath() + "/table_"+ id +"_miss_pointer.txt");
			
			int logicalSize = logicalKeys.size() > 1024 ? 1024 : logicalKeys.size();
			Integer[] randomOrder = Utils.getRandomArray(logicalSize);
			for(int i=logicalSize-1; i>=0; i--) {
				FileUtils.writeToFile(ifile, true, somConfig.getNumKseg() * somConfig.getKsegWidth() + "'h" + Utils.bitSetToHex(logicalKeys.get(randomOrder[i]), somConfig.getNumKseg() * somConfig.getKsegWidth()) + "\n");
				FileUtils.writeToFile(ofile, true, (somConfig.getNumDseg() * somConfig.getDsegWidth() + somConfig.getSramConfig().getBits() - somConfig.getDsegWidth()) + "'h" + Utils.bitSetToHex(logicalData.get(randomOrder[i]), (somConfig.getNumDseg() * somConfig.getDsegWidth() + somConfig.getSramConfig().getBits() - somConfig.getDsegWidth())) + "\n");
			}
			if(missPtrData != null)
				FileUtils.writeToFile(mfile, true, (somConfig.getNumDseg() * somConfig.getDsegWidth() + somConfig.getSramConfig().getBits() - somConfig.getDsegWidth()) + "'h" + Utils.bitSetToHex(missPtrData, (somConfig.getNumDseg() * somConfig.getDsegWidth() + somConfig.getSramConfig().getBits() - somConfig.getDsegWidth())) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer getNumKeyMemories() {
		return kx * ky;
	}
	
	public Integer getNumDataMemories() {
		return dx * dy;
	}
	
	public void emitKeyAndData(File outputDir) {
		if(isHashKey) {
			cuckoo.emitKeyAndData(outputDir);
		}
		else {
			for(int i=0; i<ky; i++) {
				for(int j=0; j<kx; j++) {
					keys[i][j].emitKeyAndData(outputDir);
				}
			}
		}
		for(int i=0; i<dy; i++) {
			for(int j=0; j<dx; j++) {
				data[i][j].emitKeyAndData(outputDir);
			}
		}
	}
	
	public void updateMemIds(List<List<Pair<Integer, Integer>>> tableTcamsList) {
		if(keys[0][0] instanceof TCAM) {
			for(int i=0; i<ky; i++) {
				for(int j=0; j<kx; j++) {
					keys[i][j].assignMemId(tableTcamsList.get(i).get(j));
				}
			}
		}
	}
	
	public int getPhysicalMemoryId(int row, int col, int segId, boolean isData) {
		return isData ? data[row][col].getId() : (isHashKey ? cuckoo.getPhysicalMemoryId(row, col, segId) : keys[row][col].getId());
	}
}
