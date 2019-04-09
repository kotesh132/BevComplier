package com.p4.p416.iface;

import java.util.List;

public interface IControl extends IIRNode {

    List<IParameter> getParameters();

    String getNameString();

    IControlBody getControlBody();

    List<ITable> getTableObjects();

    List<IActionDeclaration> getActionDeclarations();
    
    Integer getSram();

	Integer getTcam();
}