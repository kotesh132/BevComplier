package com.p4.drmt.parser;

import lombok.Data;

@Data
public class P4Transition {

	private P4Select select;
	private String nextState;
}
