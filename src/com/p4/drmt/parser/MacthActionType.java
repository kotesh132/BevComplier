package com.p4.drmt.parser;

import lombok.Getter;

public enum MacthActionType{
	OnlyMatch(1),
	OnlyAction(2),
	ActionDone(6);
	@Getter
	private final int type;
	private MacthActionType(int type){
		this.type = type;
	}
	
	public FW getFWUS(){
		return new FW(type, 4);
	}
}