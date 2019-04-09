package com.p4.p416.applications;

public interface DerivedType extends Type {
	public boolean hasMember(String memberName);
	public Symbol getMember(String memberName);
	public int getMemberBitOffset(String memberName);
	public int getMemberByteOffset(String memberName);
}
