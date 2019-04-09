package com.p4.drmt.alu;

import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.struct.IField;
import com.p4.p416.applications.Target;
import com.p4.p416.applications.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class CField implements IField{

    //@Setter private int offset;//In Bytes for Byte memory and Bits for Bit memory
    private final int size; //In Bits
    private final Target.MemoryType type;
    private final String progName;
    private final Type fieldType;
    private final int localOffset;

    @Override
    public String getUniqName() {
        return this.progName;
    }

    @Override
    public String summary() {
        return progName;//+"["+size+","//+getOffset()//+","+type.summary()
                //+"]";
    }
    @Override
    public String offsetSummary(){
        return progName+"["+size+","+getOffset()//+","+type.summary()
        +"]";
    }

    @Override
    public int getOffset() {
        return MemoryManager.getOffset(progName, fieldType)+localOffset;
    }

    @Override
    public IField addOffset(int addOff, int size){
        return new CField(size, type, progName, fieldType, addOff);
    }
}
