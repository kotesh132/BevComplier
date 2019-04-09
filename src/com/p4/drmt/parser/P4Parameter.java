package com.p4.drmt.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class P4Parameter {

	private P4Direction direction;
	private P4HeaderInst inst;
}
