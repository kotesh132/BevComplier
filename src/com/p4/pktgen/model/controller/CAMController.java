package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.CAM;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CAMController{
	
	private int id;
	private int somId;
	private int enable;
	private int tid;
	private BitSet readBitmap;
	private int missPtr;
	private List<Integer> searchEnable;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public void emitRTLConfig(File output) {
		
		String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_OFFSET.name(), id, null));
		Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_SIZE.name(), id, null)));
		Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_SIZE_IN_WORDS.name(), id, null)));
		
		BitSet camConfig = new BitSet(wordSize * numWords);
		
		Utils.setConfig(camConfig, enable, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_ENABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_ENABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(camConfig, tid, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_TABLE_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_TABLE_SIZE.name(), id, null))));
		
		Utils.setConfig(camConfig, (int) Utils.bitSetToLong(readBitmap), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_BMP_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_BMP_SIZE.name(), id, null))));
		
		Utils.setConfig(camConfig, missPtr, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_MISS_IND_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_MISS_IND_SIZE.name(), id, null))));
		
		Utils.setConfig(camConfig, searchEnable, 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_SR_EN_OFFSET.name(), id, null))), 
				Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.cam, CAM.CAM_CTLR_FIELD_SR_EN_SIZE.name(), id, null))));
		
		EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, camConfig);
	}
}
