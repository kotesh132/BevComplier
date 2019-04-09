package com.p4.p416.iface;

public interface IExpression extends IIRNode, ICTransformable, ICPPTransformable{

    String getNameString();

    boolean isRangeIndexExpression();

    boolean isFunctionCall();
}
