package com.p4.tools.graph;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import lombok.Data;

@Data
public class Edge<T> implements Summarizable{

	public final T s;
	public final T d;
	@Override
	public String summary() {
		return Utils.summary(s) + "->" + Utils.summary(d);
	}
	
}
