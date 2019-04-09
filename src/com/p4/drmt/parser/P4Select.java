package com.p4.drmt.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class P4Select {
	private List<String> expressions = new ArrayList<String>();
	private List<P4KeySet> transitions = new ArrayList<>();
}
