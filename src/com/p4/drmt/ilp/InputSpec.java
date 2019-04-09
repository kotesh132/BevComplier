package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class InputSpec {

    private int actionFieldsLimit; //Num ALUs
    private int matchUnitLimit; // Num Key Segs
    private int matchUnitSize; //KeySeg Size
    private int actionDataLimit; //Num Data Segs
    private int actionDataSize; //DataSeg Size
    private int actionProcLimit; // IPC Action Limit
    private int matchProcLimit; // IPC Match Limit
    private int matchDelay; // Match Delay
    private int actionDelay; // Action Delay
    private int dS; // ???

    private boolean solveForPacketRate;
    private int numOfProcessors;
    private int numOfContexts;
    private int packetRate;

    private boolean solveForDifferentAluTypes = false;
    private boolean handleAluTypesAtInstructionLevel = false;
    private Map<ALUType, Integer> aluTypeFieldsLimitMap;

    public InputSpec(int actionFieldsLimit,
                     int matchUnitLimit,
                     int matchUnitSize,
                     int actionDataLimit,
                     int actionDataSize,
                     int actionProcLimit,
                     int matchProcLimit,
                     int matchDelay,
                     int actionDelay,
                     int dS,
                     boolean solveForPacketRate,
                     int numOfProcessors,
                     int numOfContexts,
                     int packetRate) {
        this.actionFieldsLimit = actionFieldsLimit;
        this.matchUnitLimit = matchUnitLimit;
        this.matchUnitSize = matchUnitSize;
        this.actionDataLimit = actionDataLimit;
        this.actionDataSize = actionDataSize;
        this.actionProcLimit = actionProcLimit;
        this.matchProcLimit = matchProcLimit;
        this.matchDelay = matchDelay;
        this.actionDelay = actionDelay;
        this.dS = dS;
        this.solveForPacketRate = solveForPacketRate;
        this.numOfProcessors = numOfProcessors;
        this.numOfContexts = numOfContexts;
        this.packetRate = packetRate;
        this.aluTypeFieldsLimitMap = new LinkedHashMap<>();
        for (ALUType aluType : ALUType.values()) {
            addAluTypeFieldsLimit(aluType, 4);
        }
    }

    public void addAluTypeFieldsLimit(ALUType aluType, int fieldsLimit) {
        aluTypeFieldsLimitMap.put(aluType, fieldsLimit);
    }

    int getAluTypeFieldsLimit(ALUType aluType) {
        Integer count = aluTypeFieldsLimitMap.get(aluType);
        return count == null ? 0 : count;
    }

    public boolean isSolveForDifferentAluTypes() {
        return solveForDifferentAluTypes;
    }

    public void setSolveForDifferentAluTypes(boolean solveForDifferentAluTypes) {
        this.solveForDifferentAluTypes = solveForDifferentAluTypes;
    }

}
