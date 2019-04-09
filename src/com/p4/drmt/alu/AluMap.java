package com.p4.drmt.alu;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.p416.applications.AluInstruction;
import com.p4.p416.applications.AluMapEntry;
import com.p4.p416.gen.AssignmentStatementContextExt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.p4.drmt.cfg.DRMTConstants.*;

public class AluMap {

	public static String conddirName = "alu";

	private static final ConfigEmitUnit ac_map = new ConfigEmitUnit(BITIPTR,  NUMALUS,  NUMIADR,"ac_map.cfg");
	private static final ConfigEmitUnit inst_en = new ConfigEmitUnit(1,  NUMALUS, NUMIADR,"inst_en.cfg");
	private static final ConfigEmitUnit en_off = new ConfigEmitUnit(BITPOFF, NUMTBLE,"en_off.cfg");
	private static final ConfigEmitUnit en_vld = new ConfigEmitUnit(1 , NUMTBLE,"en_vld.cfg");
	
	static{
		init();
	}
	
	public static void init()
	{
		ac_map.pad2D(31);
		inst_en.pad2D();
		en_off.pad();
		en_vld.pad();
	}

	public static void settblvld(int index, int offset){
		en_vld.addItem(FW.ONE, index);
		en_off.addItem(offset, index);
	}
	public static void add(int tableId, int cpuId, int actionIndex){
		ac_map.setItem(tableId, actionIndex, cpuId);
		inst_en.setItem(0, actionIndex, cpuId);
	}

	public static void addInst(int tableId, int cpuId, int actionIndex){
		ac_map.setItem(tableId, actionIndex, cpuId);
		inst_en.setItem(1, actionIndex, cpuId);
	}

	public static void add(AluMapEntry entry)
	{

		for(Map.Entry<AluInstruction, Integer> cpuMapEntry : entry.getCpus().entrySet())
		{
			ac_map.setItem(entry.getTableId() ,entry.getActionIndex(), cpuMapEntry.getValue());
			//ac_map.pad(entry.getInstructionIndex());
			inst_en.setItem(0 ,entry.getActionIndex(), cpuMapEntry.getValue());
			//inst_en.pad(entry.getInstructionIndex());
		}
	}
	
	public static void add(int instructionIndex, AssignmentStatementContextExt assignmentStatementContextExt, int actionIndex)
	{
		for(int i=0; i<assignmentStatementContextExt.getInstructions().size();i++){
			ac_map.setItem(instructionIndex ,actionIndex, i);
			inst_en.setItem(1, actionIndex, i);
			
		}
	}
	
	public List<ConfigEmitUnit> getAllConfigs(){
		List<ConfigEmitUnit> ret = new ArrayList<>();
		ret.add(ac_map);
		ret.add(inst_en);
		ret.add(en_off);
		ret.add(en_vld);
		return ret;
	}

	public void emitAll(String source){
		File dirName = new File(source+File.separator+conddirName);
		dirName.mkdirs();
		for(ConfigEmitUnit c:getAllConfigs()){
			c.appendToFile(dirName.getAbsolutePath());
		}
	}
}
