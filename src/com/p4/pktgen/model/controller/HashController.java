package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.BitSet;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.Hash;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HashController {
	
	private int id;
	private int somId;
	private int enable;
	private int tableId;
	private BitSet readBitmap;
	private int indSeg;
	private BitSet keyBitmap;
	private BitSet mask;
	private int misInd;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public void emitConfig(File output) {
		
		String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_OFFSET.name(), id, null));
		Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_SIZE.name(), id, null)));
		Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_SIZE_IN_WORDS.name(), id, null)));
		
		BitSet hashConfig = new BitSet(wordSize * numWords);
		
		Utils.setConfig(hashConfig, enable, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_ENABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_ENABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, tableId, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_TABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_TABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, (int) Utils.bitSetToLong(readBitmap), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_READ_BMP_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_READ_BMP_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, indSeg, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_IND_SEG_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_IND_SEG_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, (int) Utils.bitSetToLong(keyBitmap), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_KEY_BMP_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_KEY_BMP_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, (int) Utils.bitSetToLong(mask), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_ADDR_MASK_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_ADDR_MASK_SIZE.name(), id, null))));
		
		Utils.setConfig(hashConfig, misInd, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_MISS_IND_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.hash, Hash.HASH_CTLR_MISS_IND_SIZE.name(), id, null))));
		
		EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, hashConfig);
	}
}
