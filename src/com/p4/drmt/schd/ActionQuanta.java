package com.p4.drmt.schd;

import com.p4.drmt.struct.IALUOperation;

import java.util.ArrayList;
import java.util.List;

public class ActionQuanta implements TimeQuanta{
    List<IALUOperation> aluOperations = new ArrayList<>();

    @Override
    public boolean isMatchType() {
        return false;
    }

    @Override
    public int size() {
        return aluOperations.size();
    }

    @Override
    public String summary() {
        return "Actions";
    }
}
