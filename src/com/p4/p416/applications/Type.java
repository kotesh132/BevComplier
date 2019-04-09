package com.p4.p416.applications;


public interface Type extends Symbol{
	public String getTypeName();
	public Symbol getSymbol();
	public Boolean isSame(Type type);
	public boolean isTypeCompatible(Type type1, Type type2);
	public int getTypeSize();
	public boolean isEquivalenceCompatible(Type type);
	public void setIsConstant(boolean b);
	public boolean getIsConstant();
}
