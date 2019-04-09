package com.p4.drmt.memoryManager;


import com.p4.p416.applications.Target.MemoryType;

public class InsufficientMemoryException extends Exception {

    InsufficientMemoryException(String message) {
        super(message);
    }

    InsufficientMemoryException(MemoryType memoryType, int memoryNeeded, int memoryAvailable) {
        super("For memory Type :" + memoryType.summary() + ", " + "memory needed : " + memoryNeeded + " >  available memory : " + memoryAvailable);
    }

}
