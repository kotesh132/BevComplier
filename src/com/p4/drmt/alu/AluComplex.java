package com.p4.drmt.alu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.p4.drmt.cfg.ALUComplexConstants;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.struct.IALUOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.DRMTRunnerSession;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.p416.applications.AluInstruction;
import com.p4.p416.applications.AluMapEntry;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import lombok.Data;
@Data
public class AluComplex implements Summarizable{
	private static final Logger L = LoggerFactory.getLogger(AluComplex.class);
	/*
	reg               cond_en [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITPBIT-1:0] cond_off[0:NUMALUS-1][0:NUMIADR-1];
	reg [BITTYPE-1:0] op0_type[0:NUMALUS-1][0:NUMIADR-1];
	reg [BITOLEN-1:0] op0_len [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITPOFF-1:0] op0_off [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITTYPE-1:0] op1_type[0:NUMALUS-1][0:NUMIADR-1];
	reg [BITOLEN-1:0] op1_len [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITPOFF-1:0] op1_off [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITOPCD-1:0] op_code [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITOLEN-1:0] res_len [0:NUMALUS-1][0:NUMIADR-1];
	reg [BITPOFF-1:0] res_off [0:NUMALUS-1][0:NUMIADR-1];
	reg               pt_step [0:NUMALUS-1][0:NUMIADR-1];
	 */


	public static String conddirName = "alu";

	private static final ConfigEmitUnit cond_en = new ConfigEmitUnit(1,        ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "cond_en.cfg"); //Elements = NUMALUS, Size = BITPBIT
	private static final ConfigEmitUnit cond_off = new ConfigEmitUnit(ALUComplexConstants.BITPBIT, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "cond_off.cfg");
	private static final ConfigEmitUnit op0_type = new ConfigEmitUnit(ALUComplexConstants.BITTYPE, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op0_type.cfg");
	private static final ConfigEmitUnit op0_len = new ConfigEmitUnit(ALUComplexConstants.BITOLEN, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op0_len.cfg");
	private static final ConfigEmitUnit op0_off = new ConfigEmitUnit(ALUComplexConstants.BITPOFF, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op0_off.cfg");
	private static final ConfigEmitUnit op1_type = new ConfigEmitUnit(ALUComplexConstants.BITTYPE, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op1_type.cfg");
	private static final ConfigEmitUnit op1_len = new ConfigEmitUnit(ALUComplexConstants.BITOLEN, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op1_len.cfg");
	private static final ConfigEmitUnit op1_off = new ConfigEmitUnit(ALUComplexConstants.BITPOFF, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op1_off.cfg");
	private static final ConfigEmitUnit op_code = new ConfigEmitUnit(ALUComplexConstants.BITOPCD, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "op_code.cfg");
	private static final ConfigEmitUnit res_len = new ConfigEmitUnit(ALUComplexConstants.BITOLEN,ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "res_len.cfg");
	private static final ConfigEmitUnit res_off = new ConfigEmitUnit(ALUComplexConstants.BITPOFF, ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "res_off.cfg");
	private static final ConfigEmitUnit pt_step = new ConfigEmitUnit(1,       ALUComplexConstants.NUMIADR, DRMTConstants.NUMALUS, "pt_step.cfg");
    static{
        init();
    }

	public static void init()
	{
		cond_en.pad2D();
		cond_off.pad2D();
		op0_type.pad2D();
		op0_len.pad2D();
		op0_off.pad2D();
		op1_type.pad2D();
		op1_len.pad2D();
		op1_off.pad2D();
		op_code.pad2D();
		res_len.pad2D();
		res_off.pad2D();
		pt_step.pad2D();
	}

	public static void add(ByteAluOperation aluOperation, int cpu, int instructionPointer){
		cond_en.setItem(aluOperation.cond_en(), 	cpu,  instructionPointer);
		cond_off.setItem(aluOperation.cond_off(), 	cpu, instructionPointer);
		op0_type.setItem(aluOperation.op0_type(), 	cpu, instructionPointer);
		op0_len.setItem(aluOperation.op0_len(), 	cpu, instructionPointer);
		op0_off.setItem(aluOperation.op0_off(), 	cpu, instructionPointer);
		op1_type.setItem(aluOperation.op1_type(), 	cpu, instructionPointer);
		op1_len.setItem(aluOperation.op1_len(), 	cpu, instructionPointer);
		op1_off.setItem(aluOperation.op1_off(), 	cpu, instructionPointer);
		op_code.setItem(aluOperation.op_code(), 	cpu, instructionPointer);
		res_len.setItem(aluOperation.res_len(), 	cpu, instructionPointer);
		res_off.setItem(aluOperation.res_off(), 	cpu, instructionPointer);
		pt_step.setItem(0, 					cpu, instructionPointer);
	}


	public static void add(AluMapEntry entry)
	{
		for (AluInstruction instruction: entry.getAluInstructions())
		{
			L.debug(instruction.summary());
			L.debug("At CPU "+entry.getCpus().get(instruction));
			cond_en.setItem(instruction.cond_en(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			cond_off.setItem(instruction.cond_off(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op0_type.setItem(instruction.op0_type(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op0_len.setItem(instruction.op0_len(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op0_off.setItem(instruction.op0_off(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op1_type.setItem(instruction.op1_type(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op1_len.setItem(instruction.op1_len(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op1_off.setItem(instruction.op1_off(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			op_code.setItem(instruction.op_code(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			res_len.setItem(instruction.res_len(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			res_off.setItem(instruction.res_off(), entry.getCpus().get(instruction),entry.getInstructionIndex());
			pt_step.setItem(instruction.pt_step(), entry.getCpus().get(instruction),entry.getInstructionIndex());
		}
	}

	public static void add(int instructionIndex, AssignmentStatementContextExt assignmentStatementContextExt)
	{
		List<AluInstruction> instructions = assignmentStatementContextExt.getInstructions();
		int aluIndex = 0;
		for(AluInstruction ai:instructions){
			L.debug(ai.summary());
			cond_en.setItem(ai.cond_en(), aluIndex,instructionIndex);
			cond_off.setItem(ai.cond_off(), aluIndex,instructionIndex);
			op0_type.setItem(ai.op0_type(), aluIndex,instructionIndex);
			op0_len.setItem(ai.op0_len(), aluIndex,instructionIndex);
			op0_off.setItem(ai.op0_off(), aluIndex,instructionIndex);
			op1_type.setItem(ai.op1_type(), aluIndex,instructionIndex);
			op1_len.setItem(ai.op1_len(), aluIndex,instructionIndex);
			op1_off.setItem(ai.op1_off(), aluIndex,instructionIndex);
			op_code.setItem(ai.op_code(), aluIndex,instructionIndex);
			res_len.setItem(ai.res_len(), aluIndex,instructionIndex);
			res_off.setItem(ai.res_off(), aluIndex,instructionIndex);
			pt_step.setItem(ai.pt_step(), aluIndex,instructionIndex);
			aluIndex++;
		}
	}


	public List<ConfigEmitUnit> getAllConfigs(){
		List<ConfigEmitUnit> ret = new ArrayList<>();
		ret.add(cond_en);
		ret.add(cond_off);
		ret.add(op0_type);
		ret.add(op0_len);
		ret.add(op0_off);
		ret.add(op1_type);
		ret.add(op1_len);
		ret.add(op1_off);
		ret.add(op_code);
		ret.add(res_len);
		ret.add(res_off);
		ret.add(pt_step);
		return ret;
	}


	@Override
	public String summary() {
		StringBuilder sb = new StringBuilder();
		for(ConfigEmitUnit c: getAllConfigs()){
			sb.append(c.summary());
			sb.append("\n");
		}
		return sb.toString();
	}

	public void emitAll(String source){
		File dirName = new File(source+File.separator+conddirName);
		dirName.mkdirs();
		for(ConfigEmitUnit c:getAllConfigs()){
			c.appendToFile(dirName.getAbsolutePath());
		}
	}

}
