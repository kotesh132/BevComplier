package com.p4.p416.gen;

import com.p4.p416.iface.ILValue;

import  org.antlr.v4.runtime.ParserRuleContext;
import com.p4.p416.applications.Type;

public abstract class LvalueContextExt extends AbstractBaseExt implements ILValue {
	public LvalueContextExt(ParserRuleContext context) {
		super(context);
	}

	@Override
	public String getTypeName()
	{
		return getType().getTypeName();
	}
	
	@Override
	public Type getType(){
		return this;
	}
}
