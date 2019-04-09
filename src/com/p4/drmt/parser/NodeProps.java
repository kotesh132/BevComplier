package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class NodeProps implements Summarizable{
    protected static final Logger L = LoggerFactory.getLogger(NodeProps.class);
    private final String node;
    private final List<String> incidents;
    private final List<String> adjacents;
    private final Set<Integer> outgoingDoffs;
    private final Set<Integer> outgoingDoffsPositions;
    private final Set<Integer> consolidatedOutgoingDoffs = new LinkedHashSet<>();

    public Set<Integer> getConsolidatedOutgoingDoffPoss(){
        return positions(consolidatedOutgoingDoffs);
    }


    public NodeProps(String node, List<String> incidents, List<String> adjacents, Set<Integer> outgoingDoffs){
        this.node = node;
        this.incidents = incidents;
        this.adjacents = adjacents;
        this.outgoingDoffs = outgoingDoffs;
        this.outgoingDoffsPositions = positions(outgoingDoffs);
    }

    public static Set<Integer> positions(Set<Integer> input){
        Set<Integer> ret = new LinkedHashSet<>();
        int count = 0;
        for(Integer ignored : input){
            ret.add(count++);
        }
        return ret;
    }

    public static void consolidateIncomings(List<NodeProps> nodeprops, List<Utils.Pair<String, String>> edges, Map<String, Integer> doffMap ){
        for(NodeProps node:nodeprops){
            if(node.getNode().equals("accept"))
                continue;
            L.info("For Node " +node.getNode());
            Set<Integer> allconsolidated = new LinkedHashSet<>();
            for(String allSource: node.incidents){
                NodeProps sourceNode = searchNodeProps(allSource, nodeprops);
                allconsolidated.addAll(sourceNode.outgoingDoffs);
                L.info("From source " + allSource);
            }
            L.info(Utils.summary(allconsolidated));

            for(String allSource: node.incidents){
                NodeProps sourceNode = searchNodeProps(allSource, nodeprops);
                sourceNode.consolidatedOutgoingDoffs.addAll(allconsolidated);
            }
            L.info("=====");
        }
        Map<String, Set<Integer>> ambiguous = new LinkedHashMap<>();
        Map<String, Set<Integer>> ambigousDoffs  = new LinkedHashMap<>();
        Map<String, Integer> finalPos = new LinkedHashMap<>();
        Map<String, Set<Integer>> maskPoss = new LinkedHashMap<>();
        for(Utils.Pair<String, String> edge:edges) {
            String source = edge.first();
            String dest = edge.second();
            if(dest.equals("accept"))
                continue;
            //L.info("For Edge :"+source+" -> "+dest);
            NodeProps sourceNodeProps = searchNodeProps(source, nodeprops);
            if(sourceNodeProps.getConsolidatedOutgoingDoffPoss().size()>1){
                //L.info("Ambigious positions "+ Utils.summary(sourceNodeProps.getConsolidatedOutgoingDoffPoss()));
                ambiguous.put(dest, sourceNodeProps.getConsolidatedOutgoingDoffPoss());
                ambigousDoffs.put(dest, sourceNodeProps.consolidatedOutgoingDoffs);
                int destc = doffMap.get(dest);
                if(!sourceNodeProps.consolidatedOutgoingDoffs.contains(destc)){
                    throw new UnsupportedOperationException();
                }
                int k=0;
                for( ; k<sourceNodeProps.consolidatedOutgoingDoffs.size(); k++){
                    if(k==destc)
                        break;
                }
                finalPos.put(dest, k);
                Set<Integer> maskpos = new LinkedHashSet<>(sourceNodeProps.getConsolidatedOutgoingDoffPoss());
                maskpos.remove(k);
                maskPoss.put(dest, maskpos);

            }
        }
        L.info(Utils.summary(ambiguous));
        L.info(Utils.summary(finalPos));
        L.info(Utils.summary(maskPoss));
        List<String> ambigousDest = new ArrayList<>();
        List<List<Integer>> ambiguouspositions = new ArrayList<>();
        for(Map.Entry<String, Set<Integer>> entry: ambiguous.entrySet()){
            ambigousDest.add(entry.getKey());
            ambiguouspositions.add(new ArrayList(entry.getValue()));
        }

        Utils.CartesianProductor cp = new Utils.CartesianProductor(ambiguouspositions);

        List<Utils.Pair<String, Integer>> candidates = new ArrayList<>();
        while(cp.hasNext()){
            List<Integer> perm = cp.next();
            candidates = new ArrayList<>();
            for(int i = 0; i<perm.size(); i++){
                candidates.add(Utils.Pair.of(ambigousDest.get(i), perm.get(i)));
            }
            for(Utils.Pair<String, Integer> item:candidates){
                //THIS is possible choice
                int doffc = doffMap.get(item.first());


            }

        }



    }

    private static NodeProps searchNodeProps(String seachNode, List<NodeProps> nodePropsLis){
        NodeProps ret = nodePropsLis.stream().filter(x -> x.getNode().equals(seachNode)).findFirst().get();
        return ret;
    }

    @Override
    public String summary() {
        return node+ ":\n"+
                "incidents :"+ Utils.summary(incidents)+
                "\nadjacents :"+Utils.summary(adjacents)+
                "\noutgoinDoffs :" + Utils.summary(outgoingDoffs)+
                "\noutgoinDPoss :" + Utils.summary(outgoingDoffsPositions)+
                "\nconsolidateOutgoinDoffs :" + Utils.summary(consolidatedOutgoingDoffs)+
                "\nconsolidateOutgoinDPoss :" + Utils.summary(getConsolidatedOutgoingDoffPoss())+
                "\n";
    }


    public static void main(String[] args) {
        List<List<Integer>> things  = Utils.arrList(
                Utils.arrList(0,1,2),
                Utils.arrList(0,1)
        );
        Utils.CartesianProductor<Integer> cp = new Utils.CartesianProductor<>(things);
        while(cp.hasNext()){
            List<Integer> onePerm = cp.next();
            L.info(Utils.summary(onePerm));
        }
    }


}
