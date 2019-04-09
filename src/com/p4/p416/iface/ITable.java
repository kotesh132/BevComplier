package com.p4.p416.iface;

import java.util.List;

public interface ITable extends IIRNode {

    List<IKeyElement> getKeyElements();

    List<IActionRef> getActionsRefs();

    String getTableSize();

    String getDefaultAction();

    int getTableId();

    IControl getControl();

    String getNameString();
    
    Integer getSram();
    
    Integer getTcam();
}

