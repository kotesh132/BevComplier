package com.p4.p416.iface;

import java.util.List;

public interface IBlockStatement extends IIRNode{

    List<IAssignmentStatement> getAssignmentStatements();

    List<IStatementOrDeclaration> getStatementOrDeclarations();

}
