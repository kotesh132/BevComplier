package com.p4.drmt.schd;

import com.p4.drmt.alu.*;
import com.p4.drmt.struct.IALUOperation;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ALUConfigWriter {
    private static final Logger L = LoggerFactory.getLogger(ALUConfigWriter.class);
    public static void addALUEntryForTable(int tableId, int instructionPtr, int actionIndex, int cpuId, IALUOperation ialuOperation, boolean isPredicate, int predicateOffset,
                                           ConditionalComplexRow c, CommandLineParser cp){
        L.info("TableId = "+tableId+", InstructionPtr = "+instructionPtr+", ActionIndex = "+actionIndex+", ALUID = "+cpuId+", Operation = "+ialuOperation.summary());
        File dirName = new File(cp.getOutputFile()+File.separator+"alu");
        dirName.mkdirs();
        File aluFile = new File(cp.getOutputFile()+File.separator+"alu"+ File.separator+ "alu_cfg.txt");
        AluMap.add(tableId, cpuId, actionIndex);
        FileUtils.AppendToFile(aluFile, "Map entry for table["+ tableId+"], action Index["+actionIndex+"] cpu["+cpuId+"]\n");
        if(isPredicate){
            AluMap.settblvld(tableId, predicateOffset);
        }
        FileUtils.AppendToFile(aluFile,"CPU["+cpuId+"] Instruction Pointer["+instructionPtr+"]\n");
        FileUtils.AppendToFile(aluFile, ialuOperation.offsetSummary() +"\n");
        FileUtils.AppendToFile(aluFile, "*************************\n");
        if(ialuOperation instanceof ByteAluOperation) {
            AluComplex.add((ByteAluOperation) ialuOperation, cpuId, instructionPtr);
        }else if(ialuOperation instanceof BitALUOperation){
            ConditionalConfigGenerator.generateConditionalConfig((BitALUOperation)ialuOperation, instructionPtr, c);
        }
    }

    public static void addALUEntryDirect(int instructionPtr, int actionIndex, int cpuId, IALUOperation ialuOperation, ConditionalComplexRow c, CommandLineParser cp){
        File dirName = new File(cp.getOutputFile()+File.separator+"alu");
        dirName.mkdirs();
        L.info("InstructionPtr = "+instructionPtr+", ActionIndex = "+actionIndex+", ALUID = "+cpuId+", Operation = "+ialuOperation.summary());
        File aluFile = new File(cp.getOutputFile()+File.separator+"alu"+ File.separator+ "alu_cfg.txt");
        FileUtils.AppendToFile(aluFile, "Entry for action Index["+actionIndex+"] cpu["+cpuId+"]\n");
        FileUtils.AppendToFile(aluFile,"CPU["+cpuId+"] Instruction Pointer["+instructionPtr+"]\n");
        FileUtils.AppendToFile(aluFile, ialuOperation.offsetSummary() +"\n");
        FileUtils.AppendToFile(aluFile, "*************************\n");
        AluMap.addInst(instructionPtr, cpuId, actionIndex);
        if(ialuOperation instanceof ByteAluOperation)
            AluComplex.add((ByteAluOperation) ialuOperation, cpuId, instructionPtr);
        else if(ialuOperation instanceof BitALUOperation){
            ConditionalConfigGenerator.generateConditionalConfig((BitALUOperation)ialuOperation,instructionPtr, c);
        }
    }

    public static void emit(String path){
        AluMap aluMap = new AluMap();
        aluMap.emitAll(path);
        AluComplex aluComplex = new AluComplex();
        aluComplex.emitAll(path);
    }
}
