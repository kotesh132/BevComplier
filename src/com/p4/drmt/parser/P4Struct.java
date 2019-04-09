package com.p4.drmt.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class P4Struct implements P4Type{
	private final String name;
	private final List<P4Type> types = new ArrayList<>();

	@Override
	public void pushType(P4Type type) {
		types.add(type);
	}
	
	public P4HeaderInst getHeaderByName(String hname, P4Headers hdrs){
		for(P4Type p:types){
			if(p instanceof P4HeaderInst){
				if(((P4HeaderInst) p).getName().equals(hname)){
					return (P4HeaderInst) p;
				}
			}
		}
		throw new IllegalArgumentException("No header found of name "+hname);
	}

	@Override
	public boolean isBaseType() {
		return false;
	}
}
