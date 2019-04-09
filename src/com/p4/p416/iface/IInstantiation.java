package com.p4.p416.iface;

import com.p4.p416.applications.Type;

import java.util.List;

public interface IInstantiation extends IIRNode {

    List<IArgument> getArguments();

    ITypeRef getTypeRef();

    String getNameString();

    boolean isMain();

    Type getType();
}
