package com.p4.drmt.memoryManager;


import com.p4.p416.applications.Target.MemoryType;
import com.p4.p416.applications.Type;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class MemoryInstance implements IMemoryInstance {

    private String instanceName;
    private Type type;

    @Override
    public String getInstanceName() {
        return instanceName;
    }
    
    @Override
    public Type getType(){
    	return type;
    }

    @Override
    public MemoryType getMemoryType() {
        return type.getMemoryType();
    }

    @Override
    public int getSize() {
        return getMemoryType().isBitType() ? type.getSizeInBits() : type.getSizeInBytes();
    }
}
