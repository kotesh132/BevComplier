package com.p4.p416.iface;

import java.util.HashMap;
import java.util.List;

public interface IParser extends IIRNode {

    List<IParameter> getParameters();

    String getNameString();

    List<IParserState> getParserStates();

	IParserState getStartParserState();

	HashMap<String, IParserState> getParserStatesMap();
}
