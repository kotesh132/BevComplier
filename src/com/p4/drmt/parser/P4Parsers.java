package com.p4.drmt.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
@Data
public class P4Parsers {

	private final List<P4Parser> allParsers = new ArrayList<>();
	private final Stack<P4Parser> lastparser = new Stack<>(); 
}
