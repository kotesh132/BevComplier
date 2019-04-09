package com.p4.drmt.struct;

import com.p4.drmt.alu.ALUType;
import com.p4.p416.applications.AluInstruction;
import com.p4.utils.Summarizable;

import java.util.*;

public interface IALUOperation extends MANode, Summarizable{
    public IField getLhs();
    public IOperator getOpCode();
    public IField getOp0();
    public IField getOp1();
    public List<IField> getAllOperands();
    public IField getPredicate();
    public List<IALUType> getALUType();
    public int getActionDelay();
    default public int getNumFields(){
        return 1;
    }
    public String offsetSummary();

    default IALUType getDefaultALUtype(){
        return getALUType().get(0);
    }

    @Override
    default Map<IALUType, Integer> getALUs() {
        Map<IALUType,Integer> ret = new HashMap<>();
        ret.put(getDefaultALUtype(),1);
        return ret;
    }

    default public Set<IVariable> getProducers(){
        Set<IVariable> ret = new LinkedHashSet<>();
        ret.add(getLhs());
        return ret;
    }

    default public Set<IVariable> getConsumers(){
        Set<IVariable> ret = new LinkedHashSet<>();
        ret.addAll(getAllOperands());
        ret.add(getPredicate());
        return ret;
    }

    public static enum ALUType{
        BIT,
        BYTE
    }

    public static enum Operator{
        NOOP,
        COPY,
        ADD,
        SUB,
        XOR,
        AND,
        OR,
        SHL,
        EQ,
        GT,
        LT,
        NE
    }
}
