package com.p4.p416.exceptions;

@SuppressWarnings("serial")
public class SymbolNotDefinedException extends RuntimeException {
	public SymbolNotDefinedException(String message)
	{
		super("Symbol Not Defined: " + message);
	}
}
