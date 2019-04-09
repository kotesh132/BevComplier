package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.drmt.struct.IALUType;
import com.p4.drmt.struct.IMatchNode;
import com.p4.drmt.struct.IUnit;
import com.p4.drmt.struct.MANode;
import com.p4.ds.DAGEdge;
import com.p4.ds.DAGGraph;
import com.p4.ids.IDAGEdge;
import com.p4.ids.IGraph;
import com.p4.tools.graph.Edge;
import com.p4.utils.FileUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.System.exit;

class DAGBuilder {


    IGraph<IUnitDAGVertex, IDAGEdge> getSampleDAG(InputSpec inputSpec) {
        //Build from compiler or load from file etc..
    	File dagNodesFile = new File("resources/ilp/dag/small_dag_nodes.json");
    	File edgeNodesFile = new File("resources/ilp/dag/small_dag_edges.json");
    	
        Map<String, HashMap<String, Object>> nodesMap = readFromDag(dagNodesFile.getAbsolutePath());
        Map<String, HashMap<String, Object>> edgesMap = readFromDag(edgeNodesFile.getAbsolutePath());
//        Map<String, HashMap<String, Object>> nodesMap = readFromDag("ilp/dag/switch_igress_dag_nodes.json");
//        Map<String, HashMap<String, Object>> edgesMap = readFromDag("ilp/dag/switch_igress_dag_edges.json");

        int i = 0;
        HashMap<String, IUnitDAGVertex> nameToVertexMap = new HashMap<>();
        for (String node : nodesMap.keySet()) {
            HashMap<String, Object> nodeFields = nodesMap.get(node);
            int numFields = nodeFields.get("num_fields") != null ? (Integer) (nodeFields.get("num_fields")) : 0;
            int keyWidth = nodeFields.get("key_width") != null ? (Integer) (nodeFields.get("key_width")) : 0;
            String type = (String) nodeFields.get("type");
            if ("condition".equals(type)) {
                type = "action";
                numFields = 1;
            }
            IUnitDAGVertex dagVertex = new UnitDAGVertex(null, node,
                    i++,
                    type,
                    numFields,
                    keyWidth,
                    0);
            nameToVertexMap.put(node, dagVertex);

        }

        IGraph<IUnitDAGVertex, IDAGEdge> graph = new DAGGraph<>();
        for (String edge : edgesMap.keySet()) {
            String[] srcDst = edge.split(",");
            IUnitDAGVertex srcVertex = nameToVertexMap.get(srcDst[0]);
            IUnitDAGVertex dstVertex = nameToVertexMap.get(srcDst[1]);

            assert srcVertex != null && dstVertex != null;
            String dep_type = (String) edgesMap.get(edge).get("dep_type");
            int delay = (Integer) (edgesMap.get(edge).get("delay"));
            if ("new_match_to_action".equals(dep_type) || "new_successor_conditional_on_table_result_action_type".equals(dep_type)) {
                // minimum match latency
                delay = inputSpec.getMatchDelay();
            } else if ("rmt_reverse_read".equals(dep_type) || "rmt_successor".equals(dep_type)) {
                // latency of dS, for now zero
                delay = inputSpec.getDS();
            } else if ("rmt_action".equals(dep_type) || "rmt_match".equals(dep_type)) {
                // minimum action latency
                delay = inputSpec.getActionDelay();
            } else {
                throw new RuntimeException("unable to identify dep_type");
            }

            graph.addEdge(new DAGEdge(srcVertex, dstVertex, delay), srcVertex, dstVertex);

        }

        return graph;
    }


    private Map<String, HashMap<String, Object>> readFromDag(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, HashMap<String, Object>> map = new HashMap<>();
        
//        URL resource = getClass().getClassLoader().getSystemClassLoader().getResource(fileName);
        URL resource = null;
		try {
			resource = new File(fileName).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            if (resource == null) {
                System.out.println("Could not find DAG file " + fileName);
            }
            map = mapper.readValue(resource, map.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            exit(1);
        }
        return map;
    }

    public static IGraph<IUnitDAGVertex, IDAGEdge> getDAG(Set<Edge<IUnit>> dfgEdges, Set<IUnit> allNodes) {
        IGraph<IUnitDAGVertex, IDAGEdge> graph = new DAGGraph<>();

        int index = 0;
        int numFields = 0;
        int keyWidth = 0;
        int dataWidth = 0;
        String name = "";
        Map<IUnit, IUnitDAGVertex> unitToMatchVertexMap = new HashMap<>();
        Map<IUnit, IUnitDAGVertex> unitToActionVertexMap = new HashMap<>();

        //Wrap all IUnit objects with UnitDAGVertex
        for (IUnit node : allNodes) {
            if (node instanceof MANode) {
                if (node instanceof IMatchNode) {
                    numFields = ((IMatchNode) node).getNumFields();
                    keyWidth = ((IMatchNode) node).getKeyWidth();
                    //TODO Sameek to expose getDataWidth API. Similar to keyWidth, dataWidth is max(actions input data) that will be generated.
//                    dataWidth = ((IMatchNode)node).getDataWidth();
                    name = ((IMatchNode) node).getTable().getName();
                    IUnitDAGVertex unitMatchVertex = new UnitDAGVertex(node, name + "_MATCH", index++, "match", numFields, keyWidth, dataWidth);
                    IUnitDAGVertex unitActionVertex = new UnitDAGVertex(node, name + "_ACTION", index++, "action", numFields, 0, 0);
                    unitActionVertex.setTimeNeededToComplete(((IMatchNode) node).getNodeDelay() - ((IMatchNode) node).getMatchDelay() + 1);
                    unitToMatchVertexMap.put(node, unitMatchVertex);
                    unitToActionVertexMap.put(node, unitActionVertex);
                    loadAluTypeFieldsLimitMap((MANode) node, unitActionVertex);
                    graph.addVertex(unitMatchVertex);
                    graph.addVertex(unitActionVertex);
                    int matchDelay = ((IMatchNode) node).getMatchDelay();
                    graph.addEdge(new DAGEdge(unitMatchVertex, unitActionVertex, matchDelay + 1), unitMatchVertex, unitActionVertex);
                } else {
                    name = node.summary();
                    IUnitDAGVertex unitActionVertex = new UnitDAGVertex(node, name + "_" + index, index++, "action", numFields, 0, 0);
                    unitActionVertex.setTimeNeededToComplete(((MANode) node).getNodeDelay() + 1);
                    unitToActionVertexMap.put(node, unitActionVertex);
                    loadAluTypeFieldsLimitMap((MANode) node, unitActionVertex);
                    graph.addVertex(unitActionVertex);
                }
            } else { ////else case should not arise per current design.
                assert false;
            }

        }


        for (Edge<IUnit> dfgEdge : dfgEdges) {
            IUnit srcNode = dfgEdge.getS();
            IUnitDAGVertex srcVertex = null;
            IUnitDAGVertex unitMatchVertex = null;
            IUnitDAGVertex unitActionVertex = null;

            //nodeDelay would be total Match + Max(Actions execution time) if it is IMatchNode, else it is ALU operation delay
            int nodeDelay = 0;
            if (srcNode instanceof MANode) {
                nodeDelay = ((MANode) srcNode).getNodeDelay();
            } else { ////else case should not arise per current design.
                assert false;
            }

            if (srcNode instanceof IMatchNode) {
                unitMatchVertex = unitToMatchVertexMap.get(srcNode);
                unitActionVertex = unitToActionVertexMap.get(srcNode);

                if (unitMatchVertex == null || unitActionVertex == null) {
                    throw new RuntimeException("this should not be a situation. expectation is, dfgEdges should contain edges between 'allNodes' only");
                }

                int matchDelay = ((IMatchNode) srcNode).getMatchDelay();
                nodeDelay = nodeDelay - matchDelay;
                srcVertex = unitActionVertex;

            } else {
                srcVertex = unitToActionVertexMap.get(srcNode);
                if (srcVertex == null) {
                    throw new RuntimeException("this should not be a situation. expectation is, dfgEdges should contain edges between 'allNodes' only");
                }
            }

            IUnit dstNode = dfgEdge.getD();
            IUnitDAGVertex dstVertex = null;

            if (dstNode instanceof IMatchNode) {
                unitMatchVertex = unitToMatchVertexMap.get(dstNode);
                unitActionVertex = unitToActionVertexMap.get(dstNode);

                if (unitMatchVertex == null || unitActionVertex == null) {
                    throw new RuntimeException("this should not be a situation. expectation is, dfgEdges should contain edges between 'allNodes' only");
                }

                dstVertex = unitMatchVertex;

            } else {
                dstVertex = unitToActionVertexMap.get(dstNode);
                if (dstVertex == null) {
                    throw new RuntimeException("this should not be a situation. expectation is, dfgEdges should contain edges between 'allNodes' only");
                }
            }

            graph.addEdge(new DAGEdge(srcVertex, dstVertex, nodeDelay + 1), srcVertex, dstVertex);
        }

        return graph;
    }

    private static void loadAluTypeFieldsLimitMap(MANode matchNode, IUnitDAGVertex vertex) {

        Map<IALUType, Integer> alusNeeded = matchNode.getALUs();
        for (Entry<IALUType, Integer> aluTypeEntry : alusNeeded.entrySet()) {
            if (aluTypeEntry.getKey() instanceof ALUType && aluTypeEntry.getValue() > 0) {
                vertex.addAluTypeFieldsLimit((ALUType) aluTypeEntry.getKey(), aluTypeEntry.getValue());
            }
        }
    }
}
