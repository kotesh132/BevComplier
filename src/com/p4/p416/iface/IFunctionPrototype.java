package com.p4.p416.iface;

import java.util.List;

public interface IFunctionPrototype extends IIRNode{

	String getNameString();
	
	List<IParameter> getParameters();
}
