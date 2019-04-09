package com.p4.drmt.alu;

import com.p4.drmt.struct.IALUType;

public enum ALUType implements IALUType {
    BIT_ALU("BIT_ALU"),
    SIMPLE_BYTE_ALU("SIMPLE_BYTE_ALU"),
    COMPLEX_BYTE_ALU("COMPLEX_BYTE_ALU");

    public String val;
    private ALUType(String val){
        this.val = val;
    }
    @Override
    public String summary() {
        return val;
    }
}
