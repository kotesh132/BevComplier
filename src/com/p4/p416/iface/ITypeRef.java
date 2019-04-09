package com.p4.p416.iface;

public interface ITypeRef extends IIRNode {


    String getPrefixedType();

    String getSize();

    boolean isBaseType();

    boolean isSpecializedType();

    boolean isHeaderStackType();

    boolean isTupleType();

    boolean isTypeName();

    IHeaderStackType getHeaderStackType();
}
