package com.p4.drmt.parser;


import com.p4.drmt.parser.Parameter.ParameterSet;
import lombok.Data;

@Data
public class ParserParameters {

	private final Parameter NUMADDR;
	private final Parameter NUMDOFF;
	private final Parameter WIDTH;
	private final Parameter NUMFLAG;
	
	public ParameterSet getParameterSet(){
		ParameterSet p = new ParameterSet();
		p.addParameter(NUMADDR);
		p.addParameter(NUMDOFF);
		p.addParameter(WIDTH);
		p.addParameter(NUMFLAG);
		return p;
	}
}
