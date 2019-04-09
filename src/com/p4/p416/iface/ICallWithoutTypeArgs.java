package com.p4.p416.iface;

import java.util.List;

public interface ICallWithoutTypeArgs extends IIRNode, ICTransformable {

    ILValue getLValue();

    List<IArgument> getArguments();
}
