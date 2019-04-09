package com.p4.p416.iface;

import java.util.List;

public interface IControlBody extends IIRNode {

    List<IApplyMethodCall> getApplyMethodCalls();

    List<IStatementOrDeclaration> getStatementOrDeclarations();

    IBlockStatement getBlockStatement();

}
