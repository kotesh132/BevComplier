package com.p4.p416.iface;

import java.util.List;

public interface IStruct extends IIRNode {
    List<IStructFieldList> getStructFieldList();

    List<IStructField> getStructFields();

    String getNameString();
}