package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class P4State implements Summarizable{

	private final String name;
	private P4Transition transition; 
	private final List<P4Extract> extracts = new ArrayList<>();
	private final List<P4Assign> assigns = new ArrayList<>();
	
	//private int stateId = Integer.MIN_VALUE;
	
	public static final P4State START = new P4State("start");
	public static final P4State ACCEPT = new P4State("accept");
	public static final P4State REJECT = new P4State("reject");

	@Override
	public String summary() {
		return name;
	}
}
