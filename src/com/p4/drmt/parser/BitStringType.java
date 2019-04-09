package com.p4.drmt.parser;

import lombok.Data;

@Data
public abstract class BitStringType implements P4Type {
	private final String identifier;
	private final int length;
	public String getName(){
		return identifier;
	}
	public boolean isBaseType(){
		return true;
	}
}
