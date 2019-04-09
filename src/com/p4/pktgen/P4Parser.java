package com.p4.pktgen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.p4.drmt.parser.P4Extract;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.parser.SourceDestinationSize;
import com.p4.drmt.parser.StateMeta;
import com.p4.pktgen.config.packet.PacketConfig;
import com.p4.tools.graph.Graph;

public class P4Parser {

	private Graph<P4State> parseGraph;
	private Map<String,Integer> headerIds;
	
	private P4State start = null;
	private P4State end = null;
	
	public P4Parser(Graph<P4State> graph) {
		parseGraph = graph;
		
		for(P4State node: graph.getNodes()) {
			if(node.getName().equalsIgnoreCase(P4State.START.getName()))
				start = node;
			else if(node.getName().equalsIgnoreCase(P4State.ACCEPT.getName()))
				end = node;
		}

		Map<P4State, StateMeta> map = new LinkedHashMap<>();
		headerIds = new HashMap<String,Integer>();
		for(P4State st:graph.getNodes()){
			map.put(st, new StateMeta());
		}
		StateMeta.assignStateIds(map);
		
		for(P4State st: map.keySet()) {
			headerIds.put(st.getName(), map.get(st).getStateID());
		}
		
		if(start == null || end == null) throw new RuntimeException("Unable to identify start and/or end node");
	}
	
	public P4State getParserStartState() {
		return start;
	}
	
	public P4State getParserEndState() {
		return end;
	}
	
	public Map<String,Integer> getHeaderIds() {
		return headerIds;
	}
	
	public boolean isHeaderVariable(String field) {
		for(P4State state : parseGraph.getNodes()) {
			for(P4Extract extract : state.getExtracts()) {
				if(extract.getValidField().equals(field)) {
					return true;
				}
				else {
					for(SourceDestinationSize sd : extract.getFields()) {
						if(sd.getFullName().equals(field)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public List<List<P4State>> getParserAcceptedPacketTypes(PacketConfig pktConfig) {
		List<List<P4State>> acceptedPaths = new LinkedList<List<P4State>>();
		for(List<P4State> parserPath: parseGraph.allpaths(start, end)) {
			StringBuilder path = new StringBuilder();
			for(P4State state: parserPath) {
				if(path.length() > 0)
					path.append("#");
				path.append(state.getName());
			}
			if(pktConfig.isPathValid(path.toString())){
				acceptedPaths.add(parserPath);
			}
		}
		return acceptedPaths;
	}
	
	public List<List<P4State>> getAllParserPaths() {
		return parseGraph.allpaths(start, end);
	}

	
}
