package com.p4.drmt.parser;

import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class P4Parser {
	private static final Logger L = LoggerFactory.getLogger(P4Parser.class);

	private final String name;
	private final List<P4Parameter> parameters = new ArrayList<>();
	private final List<P4State> states = new ArrayList<>();
	
	public Graph<P4State> getParseGraph(){
		Set<Edge<P4State>> edges = new LinkedHashSet<>();
		for(P4State s:states){
			if(s.getTransition().getNextState()!=null && !s.getTransition().getNextState().isEmpty()){
				P4State d = this.getState(s.getTransition().getNextState());
				L.debug(s.getName()+"->"+d.getName());
				Edge<P4State> edge = new Edge<P4State>(s, d);
				edges.add(edge);
			}else if(s.getTransition().getSelect()!=null){
				for(P4KeySet k: s.getTransition().getSelect().getTransitions()){
					P4State d = this.getState(k.getState());
					Edge<P4State> edge = new Edge<P4State>(s, d);
					edges.add(edge);
					L.debug(s.getName()+"->"+d.getName());
				}
			}
		}
		return new Graph<P4State>(edges);
	}

	private P4State getState(String name) {
		if(name.equals("accept")){
			return P4State.ACCEPT;
		}else if(name.equals("reject")){
			return P4State.REJECT;
		}
		for(P4State state:states){
			if(state.getName().equals(name)){
				return state;
			}
		}
		throw new IllegalArgumentException("Could not find state named: "+name +" in Parser"+this.name);
	}
}
