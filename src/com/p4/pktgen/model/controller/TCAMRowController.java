package com.p4.pktgen.model.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.p4.pktgen.EmitUtils;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.CAM;
import com.p4.pktgen.enums.registers.TCAMRow;
import com.p4.pktgen.enums.registers.TCAMRowAnd;
import com.p4.pktgen.mapper.Mapper;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TCAMRowController{
	
	private int id;
	private int somId;
	private List<List<Integer>> andEnableTree;
	private int[] levelSelect;
	private int[] nodeSelect;
	private int[] offset;
	@Getter private boolean isValid;
	
	private final int wordSize = 32;
	private final int maxWords = 4;
	
	public void emitRTLConfig(File output) {
		
		for(int i=0; i<levelSelect.length; i++) {
			String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_OFFSET.name(), i, id));
			Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_SIZE.name(), i, id)));
			Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_SIZE_IN_WORDS.name(), i, id)));
			
			BitSet tcamRowConfig = new BitSet(wordSize * numWords);
			
			Utils.setConfig(tcamRowConfig, levelSelect[i], 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_LVL_SEL_OFFSET.name(), i, id))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_LVL_SEL_SIZE.name(), i, id))));
			
			Utils.setConfig(tcamRowConfig, nodeSelect[i], 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_NODE_SEL_OFFSET.name(), i, id))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_NODE_SEL_SIZE.name(), i, id))));
			
			Utils.setConfig(tcamRowConfig, offset[i], 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_OFFSET_OFFSET.name(), i, id))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRow, TCAMRow.TCAM_ROW_CTRL_FIELD_OFFSET_SIZE.name(), i, id))));
		
			EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, tcamRowConfig);
		}
		
		for(int i=0; i<andEnableTree.size(); i++) {
			BitSet bs = new BitSet(andEnableTree.get(i).size());
			for(int j=0; j<andEnableTree.get(i).size(); j++) {
				if(andEnableTree.get(i).get(j) == 1)
					bs.set(j);
			}
			String baseAddr = Mapper.getValue(Mapper.getRegister(ControllerType.tcamRowAnd, TCAMRowAnd.TCAM_ROW_CTRL_AND_OFFSET.name(), i, id));
			Integer size = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRowAnd, TCAMRowAnd.TCAM_ROW_CTRL_AND_SIZE.name(), i, id)));
			Integer numWords = Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRowAnd, TCAMRowAnd.TCAM_ROW_CTRL_AND_SIZE_IN_WORDS.name(), i, id)));
			
			BitSet tcamRowConfig = new BitSet(wordSize * numWords);
			
			Utils.setConfig(tcamRowConfig, (int) Utils.bitSetToLong(bs), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRowAnd, TCAMRowAnd.TCAM_ROW_CTRL_AND_FIELD_AND_ENABLE_BMP_OFFSET.name(), i, id))), 
					Integer.parseInt(Mapper.getValue(Mapper.getRegister(ControllerType.tcamRowAnd, TCAMRowAnd.TCAM_ROW_CTRL_AND_FIELD_AND_ENABLE_BMP_SIZE.name(), i, id))));
		
			EmitUtils.emitToFile(output, somId, baseAddr, numWords, wordSize, maxWords, tcamRowConfig);
		}
	}
}
