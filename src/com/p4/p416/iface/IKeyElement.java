package com.p4.p416.iface;

import com.p4.p416.applications.Symbol;

public interface IKeyElement extends IIRNode{

	String getKeyName();

	String getKeyMatchKind();

	Symbol getKeySymbol();

	boolean isBitSliced();

	String getBitSliceFrom();

	String getBitSliceTo();

	Integer getSram();

	Integer getTcam();
}
