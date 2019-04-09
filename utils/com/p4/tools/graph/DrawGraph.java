package com.p4.tools.graph;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.p4.tools.graph.structs.TableNode;
import com.p4.utils.Utils;

public class DrawGraph {

	public static <T> void draw(Iterable<Edge<T>> edges, String op){
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("rankdir=LR");

		Set<T> nodes = nodes(edges);

		for(T n:nodes){
			if(n instanceof TableNode || n instanceof String){
				gv.addln(Utils.summary(n)+ "[shape=\"box\"]");
			}
			else{
				gv.addln(Utils.summary(n)+ "[shape=\"point\", label=\".\"]");
			}
		}

		for(Edge<T> e:edges){
			gv.addln(Utils.summary(e.s) + " -> "+ Utils.summary(e.d));
		}

		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());

		gv.increaseDpi();   // 106 dpi

		String type = "png";

		String repesentationType= "dot";
		File out = new File(op+"/out"+gv.getImageDpi()+"."+ type);   // Linux
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}

	public static <T> HashSet<T> nodes(Iterable<Edge<T>> edges){
		HashSet<T> ret = new HashSet<>();
		for(Edge<T> e:edges){
			ret.add(e.s);
			ret.add(e.d);
		}
		return ret;
	}
}
