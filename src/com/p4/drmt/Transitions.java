package com.p4.drmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.p4.tools.graph.Edge;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import lombok.Data;

@Data
public class Transitions implements Summarizable{
	List<Edge<String>> edges = new ArrayList<>();
	Stack<String> currentState = new Stack<>();
	@Override
	public String summary() {
		return Utils.summary(edges);
	}
	
}
