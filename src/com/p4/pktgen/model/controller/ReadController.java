package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.BitSet;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.Read;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadController {
	private int id;
	private int somId;
	private int enable;
	private int tableId;
	private BitSet readBitmap;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public void emitConfig(File output) {
		String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_OFFSET.name(), id, null));
		Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_SIZE.name(), id, null)));
		Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_SIZE_IN_WORDS.name(), id, null)));
		
		BitSet readConfig = new BitSet(wordSize * numWords);
		
		Utils.setConfig(readConfig, enable, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_ENABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_ENABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(readConfig, tableId, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_TABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_TABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(readConfig, (int) Utils.bitSetToLong(readBitmap), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_BMP_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.read, Read.READ_CTLR_FIELD_BMP_SIZE.name(), id, null))));
	
		EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, readConfig);
	}
}
