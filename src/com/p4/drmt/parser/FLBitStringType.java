package com.p4.drmt.parser;


public class FLBitStringType extends BitStringType {

	public FLBitStringType(String identifier, int length) {
		super(identifier, length);
	}
	public static final String id = "bit"; 
	
	@Override
	public void pushType(P4Type type) {
		throw new UnsupportedOperationException("Not yet");
	}
}
