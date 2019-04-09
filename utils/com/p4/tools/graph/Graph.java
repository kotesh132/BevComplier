package com.p4.tools.graph;

import java.util.*;

import com.p4.p416.gen.AbstractBaseExt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class  Graph<T> {
	@Getter
	private final Set<Edge<T>> edges;
	@Getter
	private final Set<T> nodes;
	
	public static<T> boolean areDisjoint(Graph<T> g1, Graph<T> g2){
		return Collections.disjoint(g1.nodes, g2.nodes) && Collections.disjoint(g1.edges, g2.edges);
	}
	
	public static<T> Graph<T> singleNodeGraph(T node){
		Set<T> nodes = new LinkedHashSet<>();
		nodes.add(node);
		Set<Edge<T>> edges = new LinkedHashSet<>();
		return new Graph<>(edges, nodes);
	}
	
	public Graph(Set<Edge<T>> edges){
		this.edges = edges;
		this.nodes = generateNodes(edges);
	}
	
	private void addEdge(Edge<T> e){
		edges.add(e);
	}
	private void removeEdge(Edge<T> e){edges.remove(e);}
	
	public Graph<T> removeSelfLoops(){
		Set<Edge<T>> np = new HashSet<Edge<T>>();
		for(Edge<T> e:edges){
			if(!e.s.equals(e.d))
				np.add(e);
		}
		Graph<T> ret =  new Graph<T>(np);
		return ret;
	}
	
	public float avgbranchingfactor(){
		float avg = 0.0f;
		int count = 0;
		for(T n:getNodes())
		{
			avg = (avg*count + adj(n).size())/(count+1);
			count++;
		}
		return avg;
	}
	public boolean containsSelfLoops(){
		for(Edge<T> e:edges){
			if(e.s.equals(e.d)){
				AbstractBaseExt sa = (AbstractBaseExt) e.s; 
				AbstractBaseExt da = (AbstractBaseExt) e.d;
				System.out.println("&&&&&&");
				System.out.println(sa.getFormattedText());
				System.out.println(da.getFormattedText());
				return true;
			}
		}
		return false;
	}
	//Outgoing Edges
	public List<T> adj(T n){
		List<T> ret = new ArrayList<>();
		for(Edge<T> e:edges){
			if(e.s.equals(n))
				ret.add(e.d);
		}
		return ret;
	}
	//Incoming Edges
	public List<T> adjTo(T n){
		List<T> ret = new ArrayList<>();
		for(Edge<T> e:edges){
			if(e.d.equals(n))
				ret.add(e.s);
		}
		return ret;
	}


	public Set<T> generateNodes(Set<Edge<T>> edges){
		Set<T> nodes = new LinkedHashSet<>();
		for(Edge<T> e:edges){
			nodes.add(e.s);
			nodes.add(e.d);
		}
		return nodes;
	}
	
	public boolean hasCycle(){
		Set<T> visited = new HashSet<>();
		Set<T> rec = new HashSet<>();
		for(T n:getNodes()){
			if(!visited.contains(n))
				if(cycleUtil(n,visited,rec))
					return true;
		}
		return false;
	}

	private boolean cycleUtil(T n, Set<T> visited, Set<T> rec) {
		visited.add(n);
		rec.add(n);
		for(T neigh:adj(n)){
			if(!visited.contains(neigh) && cycleUtil(neigh, visited, rec)){
				return true;
			}
			else if(rec.contains(neigh)){
				/*AbstractBaseExt e = (AbstractBaseExt) neigh;
				System.out.println("&&&&&&&&&&&&");
				AbstractBaseExt f = (AbstractBaseExt) n;
				System.out.println(f.getFormattedText());
				System.out.println(e.getFormattedText());*/
				return true;
			}
		}
		rec.remove(n);
		return false;
	}

	public Graph(List<Edge<T>> edges2) {
		this.edges = new HashSet<>();
		this.edges.addAll(edges2);
		this.nodes = generateNodes(this.edges);
	}
	
	public List<List<T>> allpaths(T start, T end){
		//if(hasCycle()){
			//throw new RuntimeException("Graph has cycle");
		//}
		Set<T> visited = new HashSet<>();
		Stack<T> path = new Stack<>();
		List<List<T>> ret = new ArrayList<>();
		allpathsutil(start, end, visited, path, ret);
		return ret;
	}

	private void allpathsutil(T u, T end, Set<T> visited, Stack<T> path, List<List<T>> ret) {
		visited.add(u);
		path.push(u);
		//store current path
		if(u.equals(end)){
			List<T> onepath = new ArrayList<>();
			for(T elem:path){
				onepath.add(elem);
			}
			ret.add(onepath);
		}else{
			for(T elem: adj(u)){
				if(!visited.contains(elem)){
					allpathsutil(elem, end, visited, path, ret);
				}
			}
		}
		path.pop();
		visited.remove(u);
	}
	
	public List<T> topologicalSort(){
		if(hasCycle()){
			throw new IllegalStateException("Graph contains cycle");
		}
		List<T> ret = new ArrayList<>();
		Stack<T> stack = new Stack<>();
		Set<T> visited = new HashSet<>();
		
		for(T node:getNodes()){
			if(!visited.contains(node)){
				toposort(node,visited, stack);
			}
		}
		while(!stack.isEmpty()){
			ret.add(stack.pop());
		}
		return ret;
	}
	
	public List<T> customSort(Comparator<T> c){
		List<T> ret = new ArrayList<>();
		ret.addAll(getNodes());
		Collections.sort(ret, c);
		return ret;
	}
	
	private void toposort(T node, Set<T> visited, Stack<T> stack) {
		visited.add(node);
		for(T adjnodes: adj(node)){
			if(!visited.contains(adjnodes)){
				toposort(adjnodes, visited, stack);
			}
		}
		stack.push(node);
	}
	
	public Set<Edge<T>> topoEdges(){
		List<T> nodes = topologicalSort();
		Set<Edge<T>> edges = new LinkedHashSet<>();
		for(T node:nodes){
			List<T> adjnodes = adj(node);
			for(T adjnode:adjnodes){
				Edge<T> e = new Edge<T>(node, adjnode);
				if(!edges.contains(e))
					edges.add(e);
			}
		}
		return edges;
	}

	public Set<T> nodesWithoutIncomingEdges(){
		Set<T> ret = new LinkedHashSet<>(nodes);
		for(Edge<T> edge:edges){
			if(ret.remove(edge.d));
		}
		return ret;
	}

	public void removeEdgeWithSource(T source){
		Iterator<Edge<T>> iterator = edges.iterator();
		while(iterator.hasNext()){
			Edge<T> edge = iterator.next();
			if(edge.s.equals(source)) {
				iterator.remove();
			}
		}
		nodes.remove(source);
	}

	public static void main(String[] args) {
		Graph<String> g = new Graph<>(new HashSet<Edge<String>>());
		g.addEdge(new Edge<String>("1", "2"));
		g.addEdge(new Edge<String>("1", "3"));
		g.addEdge(new Edge<String>("2", "4"));
		g.addEdge(new Edge<String>("3", "4"));
		g.addEdge(new Edge<String>("4", "5"));
		g.addEdge(new Edge<String>("4", "6"));
		g.addEdge(new Edge<String>("5", "7"));
		g.addEdge(new Edge<String>("6", "7"));
		g.addEdge(new Edge<String>("7", "8"));
		g.addEdge(new Edge<String>("7", "9"));
		g.addEdge(new Edge<String>("8", "a"));
		g.addEdge(new Edge<String>("9", "a"));
		g.addEdge(new Edge<String>("a", "b"));
		g.addEdge(new Edge<String>("a", "c"));
		g.addEdge(new Edge<String>("b", "d"));
		g.addEdge(new Edge<String>("c", "d"));
		//g.addEdge(new Edge<String>("e", "f"));
		System.out.println(g.allpaths("1", "d").size());
	}
}
