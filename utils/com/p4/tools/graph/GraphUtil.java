package com.p4.tools.graph;

import com.p4.drmt.struct.MANode;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.utils.Utils;

import java.io.File;
import java.util.*;

public class GraphUtil {

    public static<T> void drawGraph(Graph<T> graph, CommandLineParser cp){
        //Graph<T> graph = filterGraph(graph1);

        Set<Edge<T>> edges = graph.getEdges();

        Set<T> nodes = graph.getNodes();
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.addln("rankdir=LR");

        for(T node: nodes){
            if(node instanceof AbstractBaseExt){
                //System.out.println(((AbstractBaseExt) node).getFormattedText());
                gv.addln(((AbstractBaseExt) node).getFormattedText().substring(0,5) + "[shape=\"box\"]");
            }
        }

        for(Edge<T> e:edges){
            if(e.s instanceof AbstractBaseExt && e.d instanceof AbstractBaseExt){
                System.out.println(((AbstractBaseExt) e.s).getFormattedText());
                System.out.println(((AbstractBaseExt) e.d).getFormattedText());
                gv.addln(((AbstractBaseExt) e.s).getFormattedText().substring(0,5) + " -> "+ ((AbstractBaseExt) e.d).getFormattedText().substring(0,5));
            }

        }
        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());

        gv.increaseDpi();   // 106 dpi

        String type = "png";

        String repesentationType= "dot";

        File out = new File(cp.getOutputDir()+File.separator+"cfg"+gv.getImageDpi()+"."+ type);   // Linux
        gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );

    }

    /*private static<T> Graph<T> filterGraph(Graph<T> graph){
        Graph<T> ret = new Graph<>();
        List<T> sortedNodes = graph.topologicalSort();
        List<T> keptNodes = new ArrayList<>();
        for (Iterator<T> iterator = sortedNodes.iterator(); iterator.hasNext(); ) {
            T next =  iterator.next();
            if(next instanceof AbstractBaseExt){
                if(((AbstractBaseExt) next).isTableApplyNode()){
                    keptNodes.add(next);
                    iterator.remove();
                }
            }
        }


        ret.getNodes().addAll(keptNodes);
        Set<Edge<T>> retEdges = new HashSet<>();
        for(T node: sortedNodes){
            List<Edge<T>> incomingEdges = graph.allIncoming(node);
            List<Edge<T>> outgoingEdges = graph.allOutgoing(node);
            for(Edge<T> iEdge: incomingEdges){
                for(Edge<T> oEdge: outgoingEdges){
                    retEdges.add(new Edge<T>(iEdge.s, oEdge.d));
                    System.out.println( ((AbstractBaseExt)iEdge.s).getFormattedText()+"->" +((AbstractBaseExt)oEdge.d).getFormattedText());
                }
            }

        }
        ret.getEdges().addAll(retEdges);
        return ret;
    }*/
}
