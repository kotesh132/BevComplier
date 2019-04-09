package com.p4.p416.iface;

public interface IRangeIndexLvalue extends ILValue{

    IExpression getFrom();

    IExpression getTo();

    ILValue getLValue();

}
