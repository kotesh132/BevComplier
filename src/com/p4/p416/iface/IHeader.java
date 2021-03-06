package com.p4.p416.iface;

import java.util.List;

public interface IHeader extends IIRNode {
    List<IStructFieldList> getStructFieldList();

    List<IStructField> getStructFields();

    String getNameString();
}
