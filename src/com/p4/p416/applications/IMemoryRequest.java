package com.p4.p416.applications;

import lombok.Data;

public interface IMemoryRequest {
    public enum AllocType{
        ALLOCATE,
        FREE
    }
    public int hashCode();
    public boolean equals(Object obj);
    AllocType getRequestType(); // should return Alloc or Free / de-alloc
    Symbol getMemRequestSymbol(); // should return Symbol
}
