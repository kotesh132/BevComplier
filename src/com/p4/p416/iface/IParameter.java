package com.p4.p416.iface;

import com.p4.p416.applications.Type;

public interface IParameter extends IIRNode {

    String getNameString();

    Type getType();

    ITypeRef getTypeRef();
    
    String getDirection();

}
