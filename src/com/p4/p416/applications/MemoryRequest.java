package com.p4.p416.applications;

import lombok.Data;

@Data
public class MemoryRequest implements  IMemoryRequest {

    AllocType requestType;
    Symbol currentSymbol;

    public MemoryRequest(AllocType ReqType, Symbol curSymbol){
        currentSymbol = curSymbol;
        requestType = ReqType;
    }

    @Override
    public AllocType getRequestType(){
        return requestType;
    }

    @Override
    public Symbol getMemRequestSymbol(){
        return currentSymbol;
    }

    @Override
    public int hashCode() {
        return currentSymbol.hashCode() + (+31 * (requestType.ordinal() + 1));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MemoryRequest memoryRequestObj = (MemoryRequest) obj;
        if (memoryRequestObj.requestType != requestType) return false;
        if (getMemRequestSymbol().hashCode() != memoryRequestObj.getMemRequestSymbol().hashCode()) return false;
        return true;
    }

}
