package com.p4.p416.iface;

import com.p4.p416.applications.Type;

import java.util.List;

public interface IStructField extends IIRNode {

    String getNameString();

    ITypeRef getTypeRef();

    Type getType();
}
