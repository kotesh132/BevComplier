package com.p4.drmt.alu;

import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IALUType;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IOperator;
import com.p4.p416.applications.AluInstruction;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ByteAluOperation implements IALUOperation{
    private final IField lhs;
    private final AluInstruction.OpCode opCode;
    private final IField op0;
    private final IField op1;
    private final IField predicate;
    private final int actionDelay;

    @Override
    public String getId() {
        return summary();
    }

    @Override
    public int getNodeDelay() {
        return actionDelay;
    }

    @Override
    public List<IField> getAllOperands() {
        return Utils.arrList(op0,op1);
    }

    @Override
    public List<IALUType> getALUType() {
        List<IALUType> ret = new ArrayList<>();
        /*if(opCode == AluInstruction.OpCode.COPY){
            ret.add(com.p4.drmt.alu.ALUType.SIMPLE_BYTE_ALU);
        }else{
            ret.add(com.p4.drmt.alu.ALUType.COMPLEX_BYTE_ALU);
        }*/
        ret.add(com.p4.drmt.alu.ALUType.COMPLEX_BYTE_ALU);
        return ret;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.summary());
        sb.append(" = ");
        sb.append(op0.summary());
        if(op1!=null){
            sb.append(" "+ opCode.summary()+ " ");
            sb.append(op1.summary());
        }
        return sb.toString();
    }
    @Override
    public String offsetSummary(){
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.offsetSummary());
        sb.append(" = ");
        sb.append(op0.offsetSummary());
        if(op1!=null){
            sb.append(" "+ opCode.summary()+ " ");
            sb.append(op1.offsetSummary());
        }
        if(predicate!=null){
            sb.append("//Predicate = "+predicate.offsetSummary());
        }
        return sb.toString();
    }

    public int cond_en(){
        return predicate==null?0:1;
    }

    public int cond_off() {
        if(predicate !=null){
            return predicate.getOffset();
        }
        return 0;
    }

    public int op0_type() {
        return op0.getType().type/2;
    }

    public int op0_len() {
        return wordize(op0.getSize()/8);
    }
    public int op0_off(){
        return op0.getOffset();
    }

    public int op1_type() {
        if(op1!=null)
            return op1.getType().type/2;
        return 0;
    }
    public int op1_len() {
        if(op1!=null)
            return wordize(op1.getSize()/8);
        return 0;
    }
    public int op1_off(){
        if(op1!=null)
            return op1.getOffset();
        return 0;
    }
    public int res_len() {
        return wordize(lhs.getSize()/8);
    }

    public int res_off() {
        return lhs.getOffset();
    }
    public int op_code(){
        return opCode.type;

    }

    private int wordize(int input){
        if(input>=3)
            return 4;
        else
            return input;
    }
}
