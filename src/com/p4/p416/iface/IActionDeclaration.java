package com.p4.p416.iface;

import java.util.List;

public interface IActionDeclaration extends IIRNode {

    String getNameString();

    List<IParameter> getParameters();

    IBlockStatement getBlockStatement();

    int getActionId();
}
