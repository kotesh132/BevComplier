package com.p4.p416.iface;

public interface IRangeIndex extends IIRNode{

    IExpression getFrom();

    IExpression getTo();

    IExpression getExpression();
}
