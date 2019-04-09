package com.p4.drmt.memoryManager;

import com.p4.p416.applications.Type;
import com.p4.p416.applications.Target.MemoryType;

public interface IMemoryInstance {

    String getInstanceName();

    MemoryType getMemoryType();

    int getSize();
    
    Type getType();
}
