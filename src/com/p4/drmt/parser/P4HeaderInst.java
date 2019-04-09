package com.p4.drmt.parser;

import lombok.Data;

@Data
public class P4HeaderInst implements P4Type{
	private final String type;
	private final String name;
	@Override
	public void pushType(P4Type type) {
		throw new UnsupportedOperationException("Not yet");
	}
	@Override
	public boolean isBaseType() {
		return false;
	}
}
