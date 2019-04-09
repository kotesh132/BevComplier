package com.p4.packetgen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.misc.OrderedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.p416.gen.ExtendedContextGetVisitor;
import com.p4.packetgen.structures.ClassStructure;

public class HeadersInfo {
	
	private static final Logger L = LoggerFactory.getLogger(HeadersInfo.class);
	private Map<String,ClassStructure> headers;
	private ExtendedContextGetVisitor extendedContextVisitor;
	
	public HeadersInfo(){
		setHeaders(new OrderedHashMap<String,ClassStructure>());
		extendedContextVisitor = new ExtendedContextGetVisitor();
	}
	
	public ClassStructure get(String s){
		return headers.get(s);
	}

	public Map<String, ClassStructure> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, ClassStructure> headers) {
		this.headers = headers;
	}

	public List<ST> getSTs(){
		List<ST> sts = new ArrayList<ST>();
		for(String name : headers.keySet()){
			sts.add(headers.get(name).getST(extendedContextVisitor));
		}
		return sts;
	}
	
	public void add(String name,ClassStructure struct){
		headers.put(name,struct);
	}
	
}
