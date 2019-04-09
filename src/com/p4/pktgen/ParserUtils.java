package com.p4.pktgen;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.P4State;
import com.p4.pktgen.config.packet.PacketConfig;
import com.p4.tools.graph.Graph;

public class ParserUtils {
	
	private static Logger L = LoggerFactory.getLogger(ParserUtils.class);
	
	public static List<List<P4State>> getAcceptablePacketTypes(Graph<P4State> graph, PacketConfig pktConfig) {
		
		P4State start = null;
		P4State end = null;
		
		for(P4State node: graph.getNodes()) {
			if(node.getName().equalsIgnoreCase(P4State.START.getName()))
				start = node;
			else if(node.getName().equalsIgnoreCase(P4State.ACCEPT.getName()))
				end = node;
		}
		
		if(start == null || end == null) throw new RuntimeException("Unable to identify start and/or end node");
		
		List<List<P4State>> acceptedPaths = new LinkedList<List<P4State>>();
		for(List<P4State> parserPath: graph.allpaths(start, end)) {
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
}
