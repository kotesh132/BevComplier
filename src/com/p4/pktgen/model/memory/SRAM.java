package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class SRAM extends Memory{

	private Integer id;
	private Integer uid;
	private Integer startAddr;
	private Integer endAddr;
	private Integer tableId;
	private Integer segId;
	private Integer somId;
	private Integer dataWidth;
	private Integer keyWidth;
	private boolean isDataMode;
	
	private List<BitSet> memory;
	
	public SRAM(Integer depth, Integer width, Integer dataWidth, Integer uid, Integer somId, Integer segId) {
		super(depth, width);
		memory = new ArrayList<BitSet>(depth);
		this.uid = uid;
		this.somId = somId;
		this.dataWidth = dataWidth;
		this.segId = segId;
		isDataMode = true;
		for(int i=0; i<depth; i++)
			memory.add(i, null);
	}
	
	public SRAM(Integer depth, Integer width, Integer keyWidth, Integer uid, Integer somId) {
		super(depth, width);
		memory = new ArrayList<BitSet>(depth);
		this.uid = uid;
		this.somId = somId;
		this.keyWidth = keyWidth;
		isDataMode = false;
		for(int i=0; i<depth; i++)
			memory.add(i, null);
	}
	
	@Override
	public void setAddrRange(Integer start, Integer end) {
		startAddr = start;
		endAddr = end;
	}

	@Override
	public void setTableId(Integer id) {
		tableId = id;
	}

	@Override
	public void setSegId(Integer id) {
		segId = id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void addEntry(Integer index, BitSet value) {
		memory.set(index, value);
	}

	@Override
	public String emitModelConfig(Integer index) {
		return dataWidth + "'h" + Utils.bitSetToHex(memory.get(index).get(0, dataWidth));
	}

	@Override
	public void emitRTLConfig() {
		// TODO Auto-generated method stub
	}

	@Override
	public Pair<Integer, Integer> getAddrRange() {
		return Pair.of(startAddr, endAddr);
	}

	@Override
	public Integer getSegId() {
		return segId;
	}

	@Override
	public void emitKeyAndData(File outputDir) {
		
		String baseAddr = Mapper.getValue(Mapper.getMemory(ControllerType.sram, com.p4.pktgen.enums.registers.SRAM.SRAM_MEMORY_OFFSET.name(), uid, null));
		Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getMemory(ControllerType.sram, com.p4.pktgen.enums.registers.SRAM.SRAM_MEMORY_WIDTH_IN_WORDS.name(), uid, null)));
		
		Long firstLocation = (long) 0;
		if(baseAddr.contains("'h"))
			firstLocation = Long.valueOf(baseAddr.replaceFirst(".*'h", ""),16);
		else
			throw new RuntimeException("only hex values are supported");
		
		File sramFile1 = new File(outputDir.getAbsolutePath() + "/somSramCfg.sv");
		File sramFile2 = new File(outputDir.getAbsolutePath() + "/somSramCfg.txt");
		
		try {
			int entryAddress = 0;
			for(BitSet sramMem : memory) {
				//Long entryAddress = firstLocation + (numWords * index);
				//16 * 32 just a specification for a max of 16 words
				if(sramMem != null) {
					FileUtils.writeToFile(sramFile1, true, "sram_add_entry(" + somId + ", " + uid  + ", " + entryAddress + ", " + numWords + ", " + (numWords * 32) + "'h" + Utils.bitSetToHex(sramMem, getWidth()) + ");\n");
					FileUtils.writeToFile(sramFile2, true, somId + "\n" + uid  + "\n" + entryAddress + "\n" + numWords + "\n" + (numWords * 32) + "'h" + Utils.bitSetToHex(sramMem, getWidth()) + "\n");
				}
				entryAddress += numWords;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void assignMemId(Pair<Integer, Integer> id) {
		// TODO Auto-generated method stub
	}

	@Override
	public Integer getDataWidth() {
		return dataWidth;
	}

	@Override
	public String getData(Integer index, boolean isLogicalData) {
		return isLogicalData ? Utils.bitSetToHex(memory.get(index).get(0, dataWidth), dataWidth) : Utils.bitSetToHex(memory.get(index), getWidth());
	}

	@Override
	public String getNonData(Integer index) {
		return Utils.bitSetToHex(memory.get(index).get(dataWidth, getWidth()), getWidth()-dataWidth);
	}

	@Override
	public BitSet getEntry(Integer index) {
		return memory.get(index);
	}

	@Override
	public Integer getId() {
		return uid;
	}
	
}
