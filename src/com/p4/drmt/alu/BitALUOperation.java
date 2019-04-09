package com.p4.drmt.alu;

import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IALUType;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IOperator;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BitALUOperation implements IALUOperation {
    private final int numOperands;
    private final IField lhs;
    private final int actionDelay;

    @Override
    public int getNodeDelay() {
        return actionDelay;
    }

    @Override
    public String getId() {
        return summary();
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.summary()+" = ");
        if(numOperands==1){
            sb.append(opCode.summary()+ op0.summary());

        }else if(numOperands == 2){
            sb.append(op0.summary()+ " "+ opCode.summary()+ " "+ op1.summary());
        }else{
            sb.append(Utils.summary(opCode.summary(), allOperands));
        }
        return sb.toString();
    }
    @Override
    public String offsetSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.offsetSummary()+" = ");
        if(numOperands==1){
            sb.append(opCode.summary()+ op0.offsetSummary());

        }else if(numOperands == 2){
            sb.append(op0.offsetSummary()+ " "+ opCode.summary()+ " "+ op1.offsetSummary());
        }else{
            sb.append(Utils.summary(opCode.summary(), allOperands));
        }
        return sb.toString();
    }

    private final IOperator opCode;
    private final IField op0;
    private final IField op1;
    private final List<IField> allOperands = new ArrayList<>();

    @Override
    public IField getPredicate() {
        return null;
    }

    @Override
    public List<IALUType> getALUType() {
        List<IALUType> ret = new ArrayList<>();
        if(getAllOperands().size()>2){
            ret.add(com.p4.drmt.alu.ALUType.BIT_ALU);
        }else{
            //ret.add(com.p4.drmt.alu.ALUType.COMPLEX_BYTE_ALU);
            ret.add(com.p4.drmt.alu.ALUType.BIT_ALU);
        }
        return ret;
    }
}
