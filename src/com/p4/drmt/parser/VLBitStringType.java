package com.p4.drmt.parser;

public class VLBitStringType extends BitStringType {

	public VLBitStringType(String identifier, int length) {
		super(identifier, length);
	}
	
	public static final String id = "varbit"; 

	@Override
	public void pushType(P4Type type) {
		throw new UnsupportedOperationException("Not yet");
	}

}
