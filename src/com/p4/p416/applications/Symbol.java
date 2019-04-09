package com.p4.p416.applications;

import com.p4.p416.applications.Target.MemoryType;

public interface Symbol extends Scope {
    String getSymbolName();

    <T> T getSymbolKey();

    int getByteOffset();

    int getAlignedByteOffset();

    int getBitOffset();

    Type getType();

    String getTypeName();

    int getSizeInBits();

    int getSizeInBytes();

    int getAlignedSizeInBytes();

    int getValidBitOffset();

    MemoryType getMemoryType();
    
    void setNewValue(Integer value);
    
    Integer getNewValue();

}
