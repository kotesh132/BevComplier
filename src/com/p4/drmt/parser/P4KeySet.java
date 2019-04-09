package com.p4.drmt.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class P4KeySet {

	private List<String> casest = new ArrayList<String>();
	private final String state;
}
