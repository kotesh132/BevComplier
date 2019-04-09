package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.BitSet;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.TCAM;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TCAMController{
	
	private int rowId;
	private int colId;
	private int somId;
	private Integer enable;
	private BitSet tid;
	private Integer kseg;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public TCAMController(int rid, int cid) {
		enable = 0;
		tid = new BitSet();
		kseg = 0;
		rowId = rid;
		colId = cid;
	}
	
	public void emitConfig(File output) {
		
		if(enable != 0) {
			String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_OFFSET.name(), colId, rowId));
			Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_SIZE.name(), colId, rowId)));
			Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_SIZE_IN_WORDS.name(), colId, rowId)));
			
			BitSet tcamConfig = new BitSet(wordSize * numWords);
			
			Utils.setConfig(tcamConfig, enable, 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_ENABLE_OFFSET.name(), colId, rowId))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_ENABLE_SIZE.name(), colId, rowId))));
			
			Utils.setConfig(tcamConfig, (int) Utils.bitSetToLong(tid), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_TABLE_BMP_OFFSET.name(), colId, rowId))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_TABLE_BMP_SIZE.name(), colId, rowId))));
			
			Utils.setConfig(tcamConfig, kseg, 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_KSEG_OFFSET.name(), colId, rowId))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcam, TCAM.TCAM_CTRL_FIELD_KSEG_SIZE.name(), colId, rowId))));
	
			EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, tcamConfig);
		}
	}
}
