package com.p4.drmt.parser;

public interface P4Type {

	public void pushType(P4Type type);
	public String getName();
	public boolean isBaseType();
}
