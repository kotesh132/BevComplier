package com.p4.p416.iface;

import java.util.List;

public interface IActionRef extends IIRNode{

    IActionDeclaration getActionDeclaration();

    List<IArgument> getArguments();

    boolean isDefaultAction();
}
