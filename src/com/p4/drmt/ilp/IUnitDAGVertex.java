package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IMatchNode;
import com.p4.drmt.struct.IUnit;
import com.p4.ids.IDAGVertex;

import java.util.Map;

public interface IUnitDAGVertex extends IDAGVertex {

    IUnit getUnit();

    String getType();

    int getNumFields();

    int getKeyWidth();

    int getDataWidth();

    int getTime();

    int getTimeNeededToComplete();

    void setTimeNeededToComplete(int timeNeededToComplete);

    void setTime(int time);

    void addAluTypeFieldsLimit(ALUType aluType, int fieldsLimit);

    int getAluTypeFieldsLimit(ALUType aluType);

    Map<ALUType, Integer> getAluTypeFieldsLimitMap();

    default boolean isMatch() {
        return getType().equals("match");
    }

    default boolean isAluAction() {
        return getType().equals("action") && getUnit() instanceof IALUOperation;
    }

    default boolean isP4Action() {
        return getType().equals("action") && getUnit() instanceof IMatchNode;
    }
}
