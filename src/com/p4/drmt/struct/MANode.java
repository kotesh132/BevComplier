package com.p4.drmt.struct;

import com.p4.drmt.alu.ALUType;
import com.p4.utils.Summarizable;

import java.util.Map;

public interface MANode extends Summarizable,IUnit{
    public int getNodeDelay();
    public String getId();
    public Map<IALUType, Integer> getALUs();
}
