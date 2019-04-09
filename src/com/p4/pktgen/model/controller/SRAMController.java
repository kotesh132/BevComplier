package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.BitSet;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.SRAM;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SRAMController {

	private int id;
	private int somId;
	private int honourRead;
	private int honourWrite;
	private int honourHash;
	private BitSet tableId;
	private int rseg;
	private int wseg;
	private int hseg;
	private int hway;
	private int hasPtr;
	private int hasInd;
	private int startAddr;
	private int endAddr;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public void emitConfig(File output) {
		
		String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_OFFSET.name(), id, null));
		Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_SIZE.name(), id, null)));
		Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_SIZE_IN_WORDS.name(), id, null)));
		
		BitSet sramConfig = new BitSet(wordSize * numWords);
		
		Utils.setConfig(sramConfig, honourRead, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_READ_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_READ_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, honourWrite, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_WRITE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_WRITE_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, honourHash, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_HASH_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HONOR_HASH_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, (int) Utils.bitSetToLong(tableId), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_TABLE_BMP_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_TABLE_BMP_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, rseg, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_RSEG_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_RSEG_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, wseg, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_WSEG_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_WSEG_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, hseg, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HSEG_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HSEG_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, hway, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HWAY_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HWAY_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, hasPtr, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HASPTR_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HASPTR_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, hasInd, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HASIND_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_HASIND_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, startAddr, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_START_ADDR_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_START_ADDR_SIZE.name(), id, null))));
		
		Utils.setConfig(sramConfig, endAddr, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_END_ADDR_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.sram, SRAM.SRAM_CTLR_FIELD_END_ADDR_SIZE.name(), id, null))));
		
		EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, sramConfig);
	}
	
	public void emitDebugInfo(File output) {
		if(honourHash == 1) {
			FileUtils.writeToFile(output, true, "SOM-" + somId + ", SRAM-" + id + " is used for hash of table : " + tableId + ", hway : " + hway + ", hseg : " + hseg + ", startAddr : " + startAddr + ", endAddr : " + endAddr + "\n");
		}
		else if(honourRead == 1) {
			FileUtils.writeToFile(output, true, "SOM-" + somId + ", SRAM-" + id + " is used for data of table : " + tableId + ", rseg : " + rseg + ", startAddr : " + startAddr + ", endAddr : " + endAddr + "\n");
		}
	}
}
