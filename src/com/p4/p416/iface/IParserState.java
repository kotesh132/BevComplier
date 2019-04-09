package com.p4.p416.iface;

import java.util.Map;

public interface IParserState extends IIRNode, IGraphNode {

    String getNameString();
    
    void getTransitionCases(Map<String, String> transitionCases);

	IParserState getAcceptParseState();
	
	ITransitionStatement getTransitionStatement();
	
	Boolean hasNextInStateExpression();

}
