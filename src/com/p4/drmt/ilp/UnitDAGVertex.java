package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.drmt.struct.IUnit;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;


public class UnitDAGVertex implements IUnitDAGVertex {

    @Getter
    private IUnit unit;
    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String type;
    @Getter
    private int numFields;
    @Getter
    private int keyWidth;
    @Getter
    private int dataWidth;
    @Getter
    @Setter
    protected int time;
    @Getter
    @Setter
    protected int timeNeededToComplete;
    @Getter
    protected Map<ALUType, Integer> aluTypeFieldsLimitMap;


    public UnitDAGVertex(IUnit unit, String name, int id, String type, int numFields, int keyWidth, int dataWidth) {
        this.name = name;
        this.id = id;
        this.unit = unit;
        this.type = type;
        this.numFields = numFields;
        this.keyWidth = keyWidth;
        this.dataWidth = dataWidth;
        this.timeNeededToComplete = 22;
        this.aluTypeFieldsLimitMap = new LinkedHashMap<>();
    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void addAluTypeFieldsLimit(ALUType aluType, int fieldsLimit) {
        aluTypeFieldsLimitMap.put(aluType, fieldsLimit);
    }

    @Override
    public int getAluTypeFieldsLimit(ALUType aluType) {
        Integer count = aluTypeFieldsLimitMap.get(aluType);
        return count == null ? 0 : count;
    }

}
