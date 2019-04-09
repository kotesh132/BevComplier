package com.p4.pktgen.p4blocks;

import lombok.Getter;

@Getter
public class ActionParams {
	private String paramName;
	private Integer paramWidth;
	
	public ActionParams(String name, Integer width) {
		paramName = name;
		paramWidth = width;
	}
}
